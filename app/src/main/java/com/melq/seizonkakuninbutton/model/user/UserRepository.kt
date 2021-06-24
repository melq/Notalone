package com.melq.seizonkakuninbutton.model.user

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {
    companion object {
        const val collectionName = "data"
    }
    private val db = Firebase.firestore

    fun getUserName(id: String): String {
        val tag = "GET_USER_NAME"
        var name = ""
        val doc = db.collection(collectionName).document(id)
        doc.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d(tag, "DocumentSnapshot exists data: ${document.data}")
                    val user = document.data!!.toUser()
                    name = user.name
                } else {
                    Log.d(tag, "no such document")
                }
            }
            .addOnFailureListener { e ->
                Log.d(tag, "get failed with", e)
            }
        return name
    }

    fun createUser(id: String, user: User) {
        val tag = "CREATE_USER"
        val doc = db.collection(collectionName).document(id)
        doc.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d(tag, "DocumentSnapshot exists data: ${document.data}")
                } else {
                    Log.d(tag, "No such document")
                    doc.set(user)
                        .addOnSuccessListener {
                            Log.d(tag, "Document created")
                        }
                        .addOnFailureListener { e ->
                            Log.w(tag, "create failed with", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w(tag, "get failed with", e)
            }
    }

    fun reportLiving(id: String, timestamp: Timestamp) {
        val tag = "REPORT_LIVING"
        val doc = db.collection(collectionName).document(id)

        doc.update("pushHistory", FieldValue.arrayUnion(timestamp))
        Log.d(tag, "Add history: $timestamp")
    }

    private fun Map<String, Any>.toUser(): User {
        val name = this["name"] as String
        val history = this["pushHistory"] as MutableList<Timestamp>
        return User(name, history)
    }
}