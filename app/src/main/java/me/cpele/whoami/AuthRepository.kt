package me.cpele.whoami

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.preference.PreferenceManager
import net.openid.appauth.AuthState

class AuthRepository(private val application: Application?) {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val listener = { sharedPreferences: SharedPreferences, key: String ->
        if (key == PREF_AUTH_STATE) {
            val strAuthState = sharedPreferences.getString(PREF_AUTH_STATE, null)
            if (strAuthState != null) {
                val authState = AuthState.jsonDeserialize(strAuthState)
                _isLoggedIn.value = authState.isAuthorized
            } else {
                _isLoggedIn.value = false
            }
        }
    }

    init {
        listener(PreferenceManager.getDefaultSharedPreferences(application), PREF_AUTH_STATE)
        PreferenceManager.getDefaultSharedPreferences(application).registerOnSharedPreferenceChangeListener(listener)
    }

    fun persist(authState: AuthState) {
        PreferenceManager.getDefaultSharedPreferences(application).edit().putString(
                PREF_AUTH_STATE,
                authState.jsonSerializeString()
        ).apply()
    }

    companion object {
        const val PREF_AUTH_STATE = "PREF_AUTH_STATE"
    }
}