package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.domain.models.DataPoint
import com.elliottsoftware.calftracker.domain.models.NetworkResponse
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.models.fireBase.calfListToDataPointList
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.elliottsoftware.calftracker.util.Actions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
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
            emit(Response.Success(Actions.SECOND))
        }catch (e:Exception){
            Log.d("DatabaseRepository",e.message.toString())
            emit(Response.Failure(e))
        }


    }
    //todo:IMPLEMENT THE SNAPSHOT LISTENER
    override suspend fun createCalf(calf: FireBaseCalf)= callbackFlow {

            trySend(Response.Loading)
           val document = db.collection("users").document(auth.currentUser?.email!!)
                .collection("calves").document()
            calf.id = document.id

            val response = document.addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w("TAGSS", "Listen error", error)
                    trySend(Response.Failure(error))
                }
                if (value?.metadata?.isFromCache!!){
                    Log.w("TAGSS", "offline data", error)
                    document.set(calf)
                    trySend(Response.Success(true))
                }
                else{
                    document.set(calf)
                    trySend(Response.Success(true))
                }

            }


        awaitClose{
            response.remove()
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

        awaitClose{
            docRef.remove()
        }


    }

    override suspend fun getCalvesTest()= flow {
        emit(NetworkResponse.Success<List<FireBaseCalf>>(listOf()))
    }

    override suspend fun deleteCalf(id: String)= callbackFlow {
       db.collection("users").document(auth.currentUser?.email!!)
            .collection("calves").document(id).delete()
            .addOnSuccessListener { trySend(Response.Success(true))}
            .addOnFailureListener {  trySend(Response.Failure(Exception("Delete Calf Error")))}

        awaitClose()

    }

    override suspend fun updateCalf(fireBaseCalf: FireBaseCalf)= callbackFlow {
        trySend(Response.Loading)
        db.collection("users").document(auth.currentUser?.email!!)
            .collection("calves").document(fireBaseCalf.id!!).set(fireBaseCalf)
            .addOnSuccessListener { trySend(Response.Success(true)) }
            .addOnCanceledListener { trySend(Response.Failure(Exception("Update calf canceled"))) }
            .addOnFailureListener{ trySend(Response.Failure(Exception("Update calf failed"))) }

        awaitClose()
    }

    override suspend fun getDataPoints()= callbackFlow{
        trySend(Response.Loading)
        val docRef = db.collection("users")
            .document(auth.currentUser?.email!!).collection("calves")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("getDataPoints()", "Listen failed.", e)
                    trySend(Response.Failure(e))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    Log.d("getCalves()", "Current data: ${snapshot.size()}")
                    val data = snapshot.map {  document ->
                        document.toObject(FireBaseCalf::class.java)
                    }
                    val transformedData = calfListToDataPointList(data)
                    trySend(Response.Success(transformedData))
                } else {
                    Log.d("getDataPoints()", "Current data: null")
                }
            }

        awaitClose{
            docRef.remove()
        }
    }

    override suspend fun getCalvesByTagNumber(tagNumber: String)= callbackFlow {
        trySend(Response.Loading)
        val docRef = db.collection("users")
            .document(auth.currentUser?.email!!).collection("calves")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("getDataPoints()", "Listen failed.", e)
                    trySend(Response.Failure(e))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    Log.d("getCalves()", "Current data: ${snapshot.size()}")
                    val data = snapshot.map {  document ->
                        document.toObject(FireBaseCalf::class.java)
                    }
                    val filteredCalfList = data.filter { it.calfTag!!.contains(tagNumber, ignoreCase = true) }
                    trySend(Response.Success(filteredCalfList))
                } else {
                    Log.d("getDataPoints()", "Current data: null")
                }
            }

        awaitClose{
            docRef.remove()
        }
    }
}