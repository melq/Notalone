package com.melq.seizonkakuninbutton.model.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DataRepository {
    companion object {
        const val collectionName = "data"
    }
    private val db = Firebase.firestore
}