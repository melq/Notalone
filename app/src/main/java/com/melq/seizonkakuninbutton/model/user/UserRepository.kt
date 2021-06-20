package com.melq.seizonkakuninbutton.model.user

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {
    companion object {
        const val collectionName = "data"
    }
    private val db = Firebase.firestore

    fun createUser(id: String) {
        val tag = "CREATE_USER"
        val doc = db.collection(collectionName).document(id)
        doc.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d(tag, "DocumentSnapshot exists data: ${document.data}")
                } else {
                    Log.d(tag, "No such document")
                }
            }
            .addOnFailureListener { e ->
                Log.w(tag, "create failed with", e)
            }
    }

    fun makeUser(): User { // test
        return User(
            "test1",
            mutableListOf(
                Timestamp.now(),
                Timestamp.now()
            )
        )
    }
}