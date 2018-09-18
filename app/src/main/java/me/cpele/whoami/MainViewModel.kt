package me.cpele.whoami

import android.app.Application
import android.app.PendingIntent
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import net.openid.appauth.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val CLIENT_ID = "709489431311-7fkp3nqqk596et1ns7964tlfkg7v3906.apps.googleusercontent.com"
        private const val REDIRECT_URI = "com.googleusercontent.apps.709489431311-7fkp3nqqk596et1ns7964tlfkg7v3906:/oauth2redirect"
        private const val AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth"
        private const val TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token"
        private const val REQUEST_CODE_AUTH_TOKEN = 42
        private const val ACTION_AUTH_TOKEN_RESPONSE = "me.cpele.whoami.AUTH_TOKEN_RESPONSE"
    }

    private val authService by lazy { AuthorizationService(application) }

    fun signIn() {
        val configuration = AuthorizationServiceConfiguration(Uri.parse(AUTH_ENDPOINT), Uri.parse(TOKEN_ENDPOINT))
        val request = AuthorizationRequest.Builder(
                configuration,
                CLIENT_ID,
                ResponseTypeValues.CODE,
                Uri.parse(REDIRECT_URI)
        ).setScope("profile").build()
        val intent = Intent(getApplication(), MainActivity::class.java).setAction(ACTION_AUTH_TOKEN_RESPONSE)
        val pendingIntent = PendingIntent.getActivity(getApplication(), REQUEST_CODE_AUTH_TOKEN, intent, 0)
        authService.performAuthorizationRequest(request, pendingIntent)
    }

    fun handleIntent(intent: Intent) {
        if (intent.action == ACTION_AUTH_TOKEN_RESPONSE) {
            val response = AuthorizationResponse.fromIntent(intent)
            val error = AuthorizationException.fromIntent(intent)
            val authState = AuthState(response, error)
            Log.d(MainFragment::class.java.simpleName, "Auth code: ${response?.authorizationCode}")

            authState.persistTo(getApplication())

            val authorizationException = authState.authorizationException
            if (authorizationException != null) {
                Toast.makeText(
                        getApplication(),
                        "Auth error: ${authorizationException.message}",
                        Toast.LENGTH_LONG
                ).show()
                Log.w(MainFragment::class.java.simpleName, authorizationException)
            } else {
                response?.apply {
                    authService.performTokenRequest(createTokenExchangeRequest()) { response, ex ->
                        authState.update(response, ex)
                        authState.persistTo(getApplication())
                        authState.performActionWithFreshTokens(authService) { accessToken, _, _ ->
                            accessToken?.let {
                                ProfileAsyncTask(getApplication()).execute(it)
                            }
                        }
                    }
                }
            }
        }
    }
}

const val PREF_AUTH_STATE = "PREF_AUTH_STATE"

private fun AuthState.persistTo(applicationContext: Context) {
    PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString(
            PREF_AUTH_STATE,
            jsonSerializeString()
    ).apply()
}
