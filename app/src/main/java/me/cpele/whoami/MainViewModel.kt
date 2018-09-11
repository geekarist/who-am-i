package me.cpele.whoami

import android.arch.lifecycle.ViewModel
import android.util.Log

class MainViewModel : ViewModel() {

    fun signIn() {
        Log.d(javaClass.simpleName, "Yo")
    }
}
