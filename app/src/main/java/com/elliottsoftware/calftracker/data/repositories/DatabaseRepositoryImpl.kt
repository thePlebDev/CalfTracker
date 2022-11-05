package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
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
            emit(SecondaryResponse.SecondActionSuccess)
        }catch (e:Exception){
            Log.d("DatabaseRepository",e.message.toString())
            emit(SecondaryResponse.Failure(e))
        }


    }

    override suspend fun createCalf(calf: FireBaseCalf)=flow {
        Log.d("CREATE",calf.cciaNumber?:"No ccia number")
        try{
            emit(Response.Loading)
           val document = db.collection("users").document(auth.currentUser?.email!!)
                .collection("calves").document()
            calf.id = document.id
            document.set(calf)
            emit(Response.Success(true))
        }catch (e:Exception){
            Log.d("DatabaseRepository",e.message.toString())
            emit(Response.Failure(e))
        }

    }

    override suspend fun getCalves(): Flow<Response<List<FireBaseCalf>>> = callbackFlow{

            trySend(Response.Loading)
            val docRef = db.collection("users")
                .document(auth.currentUser?.email!!).collection("calves")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("getCalves()", "Listen failed.", e)
                        trySend(Response.Failure(e))
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        Log.d("getCalves()", "Current data: ${snapshot.size()}")
                       val data = snapshot.map {  document ->
                            document.toObject(FireBaseCalf::class.java)
                        }
                        trySend(Response.Success(data))
                    } else {
                        Log.d("getCalves()", "Current data: null")
                    }
                }
//                .get().await().map { document ->
//                   document.toObject(FireBaseCalf::class.java)
//                }
        awaitClose{
            docRef.remove()
        }


    }

    override suspend fun deleteCalf(id: String)= callbackFlow {
       db.collection("users").document(auth.currentUser?.email!!)
            .collection("calves").document(id).delete()
            .addOnSuccessListener { trySend(Response.Success(true))}
            .addOnFailureListener {  trySend(Response.Failure(Exception("Delete Calf Error")))}

        awaitClose()

    }
}