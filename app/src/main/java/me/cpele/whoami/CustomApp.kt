package me.cpele.whoami

import android.app.Application

class CustomApp : Application() {

    lateinit var authRepository: AuthRepository

    companion object {
        lateinit var INSTANCE: CustomApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        authRepository = AuthRepository(this)
    }
}
