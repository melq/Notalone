package com.melq.notalone.model.user

import com.google.firebase.Timestamp

class User (
    val email: String,
    var name: String,
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