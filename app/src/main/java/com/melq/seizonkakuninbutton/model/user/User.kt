package com.melq.seizonkakuninbutton.model.user

import com.google.firebase.Timestamp

class User (
    val name: String,
    val pushHistory: MutableList<Timestamp>
) {
    override fun toString(): String {
        return hashMapOf(
            "name" to name,
            "history" to pushHistory.toString()
        ).toString()
    }
}