package com.melq.seizonkakuninbutton

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.melq.seizonkakuninbutton.model.user.User
import com.melq.seizonkakuninbutton.model.user.UserRepository
import java.util.*

class MainViewModel : ViewModel() {
    companion object{
        private val repository = UserRepository()
    }
    var num = 0

    fun buttonPushed() {
        repository.createUser("test$num@example.com", makeUser(num))
        num++
    }

    private fun makeUser(num: Int): User { // test
        return User(
            "test$num",
            mutableListOf(
                Timestamp(Date(121, 5, 20, 0, 0, 0)),
                Timestamp(Date(121, 5, 20, num, 0, 15))
            )
        )
    }
}