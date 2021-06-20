package com.melq.seizonkakuninbutton

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.melq.seizonkakuninbutton.model.user.User
import com.melq.seizonkakuninbutton.model.user.UserRepository

class MainViewModel : ViewModel() {
    companion object{
        private val repository = UserRepository()
    }
    val id: String = ""
    val name: String = ""

    fun buttonPushed() {
        if (id == "") return
        repository.reportLiving(id, Timestamp.now())
    }

    fun createUser() {
        if (id == "" || name == "") return
        repository.createUser(
            id,
            User(
                name,
                mutableListOf(
                    Timestamp.now()
                )
            ))
    }
}