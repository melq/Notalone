package com.melq.notalone.model.user

class User (
    val id: String,
    val email: String,
    var name: String,
    val pushHistory: MutableList<Map<String, Any>>,
    val watchList: MutableList<Map<String, String>>

) {
    constructor() : this("", "", "", mutableListOf(), mutableListOf())

    override fun toString(): String {
        return hashMapOf(
            "id" to id,
            "email" to email,
            "name" to name,
            "pushHistory" to pushHistory.toString(),
            "watchList" to watchList.toString()
        ).toString()
    }
}