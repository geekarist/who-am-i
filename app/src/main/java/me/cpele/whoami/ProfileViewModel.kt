package me.cpele.whoami

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class ProfileViewModel(authRepo: AuthRepository) : ViewModel() {

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(ProfileViewModel(authRepository)) as T
        }
    }

    val navigationEvent: LiveData<LiveEvent<Int>> = Transformations.map(authRepo.isLoggedIn) { isLoggedIn ->
        if (!isLoggedIn) LiveEvent(R.id.navigate_to_login) else null
    }
}
