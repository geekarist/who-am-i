package me.cpele.whoami

import android.app.Application
import android.arch.persistence.room.Room
import com.google.gson.Gson
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
    private val gson by lazy { Gson() }
    private val personRepository by lazy { PersonRepository(gson) }

    val profileViewModelFactory by lazy {
        ProfileViewModel.Factory(
                this,
                authHolder,
                personRepository,
                authService
        )
    }

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
