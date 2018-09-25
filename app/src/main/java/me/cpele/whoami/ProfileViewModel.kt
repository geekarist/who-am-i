package me.cpele.whoami

import android.app.Application
import android.arch.lifecycle.*
import net.openid.appauth.AuthorizationService

class ProfileViewModel(
        application: Application,
        authRepo: AuthRepository,
        authService: AuthorizationService
) : AndroidViewModel(application) {

    init {
        authRepo.authState?.performActionWithFreshTokens(authService) { accessToken, _, _ ->
            accessToken?.let {
                ProfileAsyncTask(getApplication()).execute(it)
            }
        }
    }

    val navigationEvent: LiveData<LiveEvent<Int>> = Transformations.map(authRepo.isLoggedIn) { isLoggedIn ->
        if (!isLoggedIn) LiveEvent(R.id.navigate_to_login) else null
    }

    val name = MutableLiveData<String>().apply { value = "Unknown person" }

    class Factory(
            private val application: Application,
            private val authRepository: AuthRepository,
            private val authService: AuthorizationService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(ProfileViewModel(application, authRepository, authService)) as T
        }
    }
}
