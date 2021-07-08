package com.melq.notalone.model.user

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

    fun createUser(id: String, user: User, onSuccess: () -> Unit) {
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
                            onSuccess()
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

    fun getUserData(id: String, onSuccess: (User) -> Unit): User { // 取得待機がよくわからんので、関数を渡している
        val tag = "GET_USER_DATA"
        var user = User()
        val doc = db.collection(collectionName).document(id)
        doc.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(tag, "DocumentSnapshot exists. data: ${document.data}")
                    user = document.data!!.toUser()
                } else {
                    Log.d(tag, "no such document")
                }
                onSuccess(user)
            }
            .addOnFailureListener { e ->
                Log.d(tag, "get failed with", e)
            }
        return user // 待ち方わからんので、 User() が返ってる
    }

    fun getUserName(id: String, onSuccess: (String) -> Unit): String {
        val tag = "GET_USER_NAME"
        var name = ""
        val doc = db.collection(collectionName).document(id)
        doc.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d(tag, "DocumentSnapshot exists data: ${document.data}")
                    name = document.data!!.toUser().name
                } else {
                    Log.d(tag, "no such document")
                }
                onSuccess(name)
            }
            .addOnFailureListener { e ->
                Log.d(tag, "get failed with", e)
            }
        return name // 待ち方わからんので name = "" が返ってる
    }

    fun reportLiving(id: String, timestamp: Timestamp) {
        val tag = "REPORT_LIVING"
        val doc = db.collection(collectionName).document(id)

        doc.update("pushHistory", FieldValue.arrayUnion(timestamp))
        Log.d(tag, "Add history: $timestamp")
    }

    fun updateName(id: String, newName: String, onSuccess: () -> Unit) {
        val tag = "UPDATE_NAME"
        val doc = db.collection(collectionName).document(id)
        doc.update("name", newName)
            .addOnSuccessListener {
                Log.d(tag, "DocumentSnapshot successfully updated!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error updating document", e)
            }
    }

    private fun Map<String, Any>.toUser(): User {
        val email = this["email"] as String
        val name = this["name"] as String
        val history = this["pushHistory"] as MutableList<Timestamp>
        return User(email, name, history)
    }
}