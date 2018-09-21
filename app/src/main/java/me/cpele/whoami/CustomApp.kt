package me.cpele.whoami

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import net.openid.appauth.AuthorizationService

class CustomApp : Application() {

    val authRepository: AuthRepository by lazy { AuthRepository(this) }
    val authService by lazy { AuthorizationService(this) }
    val profileViewModelFactory by lazy { ProfileViewModel.Factory(this, authRepository, authService) }

    companion object {
        lateinit var INSTANCE: CustomApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}
