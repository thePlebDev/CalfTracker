package com.elliottsoftware.calftracker.data.sources.implementations

import com.elliottsoftware.calftracker.data.sources.DatabaseSource
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class FireBaseFireStore:DatabaseSource {
    private val db: FirebaseFirestore = Firebase.firestore

    override fun createUser(email: String, username: String): Flow<Response<Boolean>> = callbackFlow{
        trySend(Response.Loading)
        val user = hashMapOf(
            "email" to email,
            "username" to username
        )
        db.collection("users").document(email).set(user)
            .addOnSuccessListener {
                trySend(Response.Success(true))

            }
            .addOnFailureListener {
                    e ->
                trySend(Response.Failure(Exception(" Error! Please try again")))
            }

        awaitClose()
    }

    override fun createCalf(calf: FireBaseCalf, userEmail: String): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)

            val collection = db.collection("users").document(userEmail)
                .collection("calves")
            val document = collection.document()
            val id = document.id
            calf.id = id

            collection.document(id).set(calf)
                .addOnSuccessListener { document ->
                    Timber.d( "DocumentSnapshot written with ID: ${calf.id}")
                    trySend(Response.Success(true))
                }
                .addOnFailureListener { e ->
                    Timber.e( "Error adding document", e)
                    trySend(Response.Failure(e))
                }

        awaitClose()
    }

    override fun getCalves(
        queryLimit: Long,
        userEmail: String
    ): Flow<Response<List<FireBaseCalf>>> = callbackFlow{
        trySend(Response.Loading)


        val query = db.collection("users")
            .document(userEmail)
            .collection("calves")
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(queryLimit)



        val  docRef = query.addSnapshotListener { snapshot, e ->
            //error handling for snapshot listeners
            if (e != null) {

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

                    trySend(Response.Success(data))
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
}