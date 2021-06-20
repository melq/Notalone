package com.melq.seizonkakuninbutton

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.melq.seizonkakuninbutton.model.user.User
import com.melq.seizonkakuninbutton.model.user.UserRepository

class MainViewModel : ViewModel() {
    companion object{
        private val repository = UserRepository()
    }

    fun buttonPushed() {
        repository.createUser("test_user", makeUser())
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