package com.melq.seizonkakuninbutton

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.melq.seizonkakuninbutton.model.user.User
import com.melq.seizonkakuninbutton.model.user.UserRepository

class MainViewModel : ViewModel() {
    companion object{
        private val repository = UserRepository()
    }
    var id: String = ""
    var name: String = ""

    var eMessage: MutableLiveData<Int> = MutableLiveData(0)

    fun setUserName(id: String, name: String) {
        this.id = id
        this.name = name
    }

    fun buttonPushed() {
        if (id == "") return
        repository.reportLiving(id, Timestamp.now())
    }

    fun loginPushed(id: String, password: String) {
        if (id.isBlank() || password.isBlank()) {
            eMessage.value = R.string.enter_info
            return
        }

        val name = repository.getUserName(id)
        if (name.isNotBlank()) {
            setUserName(id, name)

            // ログイン処理を書く

        } else {
            eMessage.value = R.string.not_registered
        }
    }

    fun createPushed(name: String, id: String, password: String) {
        if (name.isBlank() || id.isBlank() || password.isBlank()) {
            eMessage.value = R.string.enter_info
            return
        }

        // アカウント作成処理を書く

        createUser()
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