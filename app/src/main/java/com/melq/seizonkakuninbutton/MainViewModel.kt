package com.melq.seizonkakuninbutton

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.melq.seizonkakuninbutton.model.user.User
import com.melq.seizonkakuninbutton.model.user.UserRepository

class MainViewModel : ViewModel() {
    companion object{
        private val repository = UserRepository()
        private val auth = Firebase.auth
    }
    var id: String = ""
    var name: String = ""

    var eMessage: MutableLiveData<Int> = MutableLiveData(0)
    var done: MutableLiveData<Boolean> = MutableLiveData(false)

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

        repository.getUserName(id) { name ->
            if (name.isNotBlank()) {
                Log.d("LOGIN_PUSHED", "$id, $name")
                setUserName(id, name)

                // ログイン処理を書く

                done.value = true
            } else {
                eMessage.value = R.string.not_registered
            }
        }
    }

    fun createPushed(name: String, id: String, password: String) {
        if (name.isBlank() || id.isBlank() || password.isBlank()) {
            eMessage.value = R.string.enter_info
            return
        }

        // アカウント作成処理を書く

        setUserName(id, name)
        if (id == "" || name == "") return
        repository.createUser(
            id,
            User(
                name,
                mutableListOf(
                    Timestamp.now()
                )
            )) {
            done.value = true
        }
    }
}