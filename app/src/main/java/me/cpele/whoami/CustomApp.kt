package me.cpele.whoami

import android.app.Application

class CustomApp : Application() {

    companion object {
        lateinit var INSTANCE: CustomApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}
