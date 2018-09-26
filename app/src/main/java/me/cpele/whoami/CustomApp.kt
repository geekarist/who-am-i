package me.cpele.whoami

import android.app.Application
import android.arch.persistence.room.Room
import net.openid.appauth.AuthorizationService

class CustomApp : Application() {

    private val customDatabase by lazy {
        Room.databaseBuilder(
                this,
                CustomDatabase::class.java,
                CustomDatabase::class.java.simpleName
        ).build()
    }

    val authHolder: AuthHolder by lazy { AuthHolder(customDatabase.authDao()) }
    val authService by lazy { AuthorizationService(this) }

    val profileViewModelFactory by lazy { ProfileViewModel.Factory(this, authHolder, authService) }
    val loginViewModelFactory by lazy {
        LoginViewModel.Factory(
                AuthHolder(customDatabase.authDao()),
                CustomApp.INSTANCE
        )
    }

    companion object {
        lateinit var INSTANCE: CustomApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}
