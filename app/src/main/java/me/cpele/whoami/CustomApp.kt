package me.cpele.whoami

import android.app.Application
import net.openid.appauth.AuthorizationService

class CustomApp : Application() {

    val authHolder: AuthHolder by lazy { AuthHolder(this) }
    val authService by lazy { AuthorizationService(this) }
    val profileViewModelFactory by lazy { ProfileViewModel.Factory(this, authHolder, authService) }

    companion object {
        lateinit var INSTANCE: CustomApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}
