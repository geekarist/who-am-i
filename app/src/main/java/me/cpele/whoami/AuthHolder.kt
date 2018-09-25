package me.cpele.whoami

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.preference.PreferenceManager
import net.openid.appauth.AuthState

class AuthHolder(private val application: Application?) {

    private val listener = { sharedPreferences: SharedPreferences, key: String ->
        if (key == PREF_AUTH_STATE) {
            val strAuthState = sharedPreferences.getString(PREF_AUTH_STATE, null)
            if (strAuthState != null) {
                val authState = AuthState.jsonDeserialize(strAuthState)
                _state.value = authState
            }
        }
    }

    private val _state = MutableLiveData<AuthState>()
    val state: LiveData<AuthState>
        get() = _state

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