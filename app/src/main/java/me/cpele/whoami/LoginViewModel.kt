package me.cpele.whoami

import android.app.Application
import android.app.PendingIntent
import android.arch.lifecycle.*
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import net.openid.appauth.*

class LoginViewModel(
        application: Application,
        authRepository: AuthRepository
) : AndroidViewModel(application) {

    class Factory(
            private val authRepository: AuthRepository,
            private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(LoginViewModel(application, authRepository)) as T
        }

    }

    val loginEvent: LiveData<LiveEvent<Unit>> = Transformations.map(authRepository.isLoggedIn) {
        if (it) LiveEvent(Unit) else null
    }

    fun signIn() {
        val application: Application = getApplication()
        application.startService(Intent(application, LoginService::class.java)
                .setAction(LoginService.ACTION_REQUEST_AUTH))
    }
}