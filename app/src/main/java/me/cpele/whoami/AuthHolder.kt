package me.cpele.whoami

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import net.openid.appauth.AuthState

class AuthHolder(private val authDao: AuthDao) {
    val state: LiveData<AuthState?> = Transformations.map(authDao.get()) { it?.state }
    fun persist(authState: AuthState) {
        authDao.clear()
        authDao.insert(Auth(state = authState))
    }
}