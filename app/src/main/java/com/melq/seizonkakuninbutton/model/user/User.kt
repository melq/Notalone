package com.melq.seizonkakuninbutton.model.user

import com.google.firebase.Timestamp

class User (
    val email: String,
    val name: String,
    val pushHistory: MutableList<Timestamp>
) {
    constructor() : this("", "", mutableListOf())

    override fun toString(): String {
        return hashMapOf(
            "email" to email,
            "name" to name,
            "history" to pushHistory.toString()
        ).toString()
    }
}