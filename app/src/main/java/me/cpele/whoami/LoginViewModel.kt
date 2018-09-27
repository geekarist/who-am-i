package me.cpele.whoami

import android.app.Application
import android.arch.lifecycle.*
import android.content.Intent
import android.util.Log

class LoginViewModel(
        application: Application,
        authHolder: AuthHolder
) : AndroidViewModel(application) {

    class Factory(
            private val authHolder: AuthHolder,
            private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(LoginViewModel(application, authHolder)) as T
        }
    }

    val loginEvent: LiveData<LiveEvent<Unit>> = Transformations.map(authHolder.state) {
        if (true == it?.isAuthorized) {
            Log.d(this::class.java.simpleName, "Auth holder state is authorized")
            LiveEvent(Unit)
        } else {
            Log.d(
                    javaClass.simpleName,
                    "Auth holder state is not authorized: ${authHolder.state.value?.jsonSerializeString()}"
            )
            null
        }
    }

    fun signIn() {
        val application: Application = getApplication()
        application.startService(Intent(application, LoginService::class.java)
                .setAction(LoginService.ACTION_REQUEST_AUTH))
    }
}