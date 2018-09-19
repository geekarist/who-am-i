package me.cpele.whoami

import android.app.Application

class CustomApp : Application() {

    companion object {
        var INSTANCE: CustomApp? = null
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}
