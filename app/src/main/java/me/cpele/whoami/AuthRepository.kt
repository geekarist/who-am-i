package me.cpele.whoami

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

class AuthRepository {
    private val _isLoggedIn = MutableLiveData<Boolean>().apply { value = false }
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn
}
