package com.melq.notalone.model.user

class User (
    val id: String,
    val email: String,
    var name: String,
    val pushHistory: MutableList<Map<String, Any>>
) {
    constructor() : this("", "", "", mutableListOf())

    override fun toString(): String {
        return hashMapOf(
            "id" to id,
            "email" to email,
            "name" to name,
            "history" to pushHistory.toString()
        ).toString()
    }
}