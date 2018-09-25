package me.cpele.whoami

import android.app.Application
import android.arch.lifecycle.*
import net.openid.appauth.AuthorizationService

class ProfileViewModel(
        application: Application,
        authHolder: AuthHolder,
        authService: AuthorizationService
) : AndroidViewModel(application) {

    val navigationEvent: LiveData<LiveEvent<Int>> =
            Transformations.map(authHolder.state) { state ->
                if (!state.isAuthorized) LiveEvent(R.id.navigate_to_login) else null
            }

    val name = MutableLiveData<String>().apply { value = "Unknown person" }

    class Factory(
            private val application: Application,
            private val authHolder: AuthHolder,
            private val authService: AuthorizationService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(ProfileViewModel(application, authHolder, authService)) as T
        }
    }
}
