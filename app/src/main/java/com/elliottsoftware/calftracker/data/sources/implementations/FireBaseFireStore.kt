package com.elliottsoftware.calftracker.data.sources.implementations

import com.elliottsoftware.calftracker.data.sources.DatabaseSource
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
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
}