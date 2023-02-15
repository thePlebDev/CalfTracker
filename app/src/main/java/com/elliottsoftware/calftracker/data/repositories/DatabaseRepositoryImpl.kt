package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.domain.models.NetworkResponse
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.elliottsoftware.calftracker.util.Actions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class DatabaseRepositoryImpl(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val auth: FirebaseAuth= Firebase.auth
):DatabaseRepository {

    override suspend fun createUser(email: String, username: String)= flow{
        val user = hashMapOf(
            "email" to email,
            "username" to username
        )
        try{
            db.collection("users").document(email).set(user).await()
            emit(Response.Success(Actions.SECOND))
        }catch (e:Exception){
            Timber.e(e)
            emit(Response.Failure(e))
        }


    }
    //todo:IMPLEMENT THE SNAPSHOT LISTENER
    override suspend fun createCalf(calf: FireBaseCalf)= callbackFlow {

            trySend(Response.Loading)
        try{
            val collection = db.collection("users").document(auth.currentUser?.email!!)
                .collection("calves")
            val document = collection.document()
            val id = document.id
            calf.id = id

            Timber.d(calf.toString())
            collection.document(id).set(calf)
                .addOnSuccessListener { document ->
                    Timber.d( "DocumentSnapshot written with ID: ${calf.id}")
                    trySend(Response.Success(true))
                }
                .addOnFailureListener { e ->
                    Timber.e( "Error adding document", e)
                    trySend(Response.Failure(e))
                }

        }catch (e:Exception){
            Timber.e(e)
            trySend(Response.Failure(e))
        }




        awaitClose()

    }

    //addSnapshotListener is needed for the real time updates

    override suspend fun getCalves(): Flow<Response<List<FireBaseCalf>>> = callbackFlow{

            trySend(Response.Loading)


            val query = db.collection("users")
                .document(auth.currentUser?.email!!).collection("calves").orderBy("date")

           val  docRef = query.addSnapshotListener { snapshot, e ->
               //error handling for snapshot listeners
                if (e != null) {
                    Timber.d("BELOW")
                    Timber.d(auth.currentUser.toString())
                    Timber.e(e)


                    return@addSnapshotListener

                }



                if (snapshot != null) {

                    val data = snapshot.mapNotNull {  document ->


                        document.toObject<FireBaseCalf>()
                    }

                    if(data.isNotEmpty() && data[0].calftag == null){
                        Timber.e(data.toString())
                        trySend(Response.Failure(Exception("FAILED")))
                    }else{
                        Timber.d(data.toString())
                        trySend(Response.Success(data.reversed()))
                    }

                } else {
                    Timber.d("current data null")
                    trySend(Response.Failure(Exception("FAILED")))
                }
            }




        awaitClose{
            docRef.remove()
        }

    }

    override suspend fun getCalvesTest()= flow {
        emit(NetworkResponse.Success<List<FireBaseCalf>>(listOf()))
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



    override suspend fun getCalvesByTagNumber(tagNumber: String)= callbackFlow {
        trySend(Response.Loading)
        val docRef = db.collection("users")
            .document(auth.currentUser?.email!!).collection("calves").orderBy("date")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {

                    Timber.e(e)
                    trySend(Response.Failure(e))
                    return@addSnapshotListener
                }

                if (snapshot != null) {

                    val data = snapshot.map {  document ->
                        document.toObject(FireBaseCalf::class.java)
                    }

                    val filteredCalfList = data.filter { it.calftag!!.contains(tagNumber, ignoreCase = true) }

                    trySend(Response.Success(filteredCalfList.reversed()))
                } else {
                    Timber.e("getCalvesByTagNumber data is null")

                }
            }

        awaitClose{
            docRef.remove()
        }
    }
}