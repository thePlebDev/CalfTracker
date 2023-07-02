package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.data.sources.AuthenticationSource
import com.elliottsoftware.calftracker.data.sources.DatabaseSource
import com.elliottsoftware.calftracker.data.sources.implementations.FireBaseAuthentication
import com.elliottsoftware.calftracker.data.sources.implementations.FireBaseFireStore
import com.elliottsoftware.calftracker.domain.models.NetworkResponse
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.elliottsoftware.calftracker.util.Actions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class DatabaseRepositoryImpl(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val auth: FirebaseAuth= Firebase.auth,
    private val authenticationSource: AuthenticationSource = FireBaseAuthentication(),
    private val databaseSource: DatabaseSource = FireBaseFireStore()
):DatabaseRepository {


    //todo:add email to function call
    override  fun createCalf(calf: FireBaseCalf,userEmail:String):Flow<Response<Boolean>> {

          val items = databaseSource.createCalf(calf,userEmail)
              .catch { cause: Throwable ->
                  emit(Response.Failure(Exception(" Error! Please try again")))
              }
        return items

    }

    //addSnapshotListener is needed for the real time updates

    override fun getCalves(queryLimit:Long,userEmail: String): Flow<Response<List<FireBaseCalf>>>{
        val items = databaseSource.getCalves(queryLimit,userEmail)
            .catch { cause: Throwable ->
                emit(Response.Failure(Exception(" Error! Please try again")))
            }

        return items

    }

    override suspend fun deleteCalf(id: String)= callbackFlow {
        Timber.d("IDBELOW")
        Timber.d(id)
       db.collection("users").document(auth.currentUser?.email!!)
            .collection("calves").document(id).delete()
            .addOnSuccessListener {
                Timber.d("DELETE SUCCESS")
                trySend(Response.Success(true))
            }
            .addOnFailureListener {
                Timber.d("DELETE FAIL")
                Timber.d(it)
                trySend(Response.Failure(Exception("Delete Calf Error")))
            }

        awaitClose()

    }



    override suspend fun updateCalf(fireBaseCalf: FireBaseCalf)= callbackFlow {
        trySend(Response.Loading)
        db.collection("users").document(auth.currentUser?.email!!)
            .collection("calves").document(fireBaseCalf.id!!).set(fireBaseCalf)
//            .update(
//                "calfTag",fireBaseCalf.calfTag,
//                "cowTag",fireBaseCalf.cowTag,
//                "cciaNumber",fireBaseCalf.cciaNumber,
//                "sex",fireBaseCalf.sex,
//                "birthWeight",fireBaseCalf.birthWeight,
//            )
            .addOnSuccessListener { trySend(Response.Success(true)) }
            .addOnCanceledListener {
                Timber.d("CANCELED")
                trySend(Response.Failure(Exception("Update calf canceled")))
            }
            .addOnFailureListener{
                Timber.d("FAILED")
                trySend(Response.Failure(Exception("Update calf failed")))
            }

        awaitClose()
    }


    override suspend fun getCalvesByTagNumber(tagNumber:String)= callbackFlow {
        trySend(Response.Loading)
        db.collection("users")
            .document(auth.currentUser?.email!!).collection("calves")
            .orderBy("date", Query.Direction.DESCENDING)
            .whereEqualTo("calftag", tagNumber)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val data = document.map {  document ->
                        document.toObject(FireBaseCalf::class.java)
                    }
                    Timber.tag("CALVESSEARCH").d(data.toString())
                    trySend(Response.Success(data))
                } else {
                    Timber.tag("CALVESSEARCH").d("NOTHING FOUND")
                }
            }
            .addOnFailureListener { exception ->
                Timber.tag("CALVESSEARCH").d(exception.toString())
                trySend(Response.Failure(exception))
            }

    awaitClose()

    }
}