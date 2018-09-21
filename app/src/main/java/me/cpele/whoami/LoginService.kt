package me.cpele.whoami

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.util.Log
import net.openid.appauth.*

class LoginService : IntentService(this::class.java.simpleName) {

    companion object {
        private const val CLIENT_ID = "709489431311-7fkp3nqqk596et1ns7964tlfkg7v3906.apps.googleusercontent.com"
        private const val REDIRECT_URI = "com.googleusercontent.apps.709489431311-7fkp3nqqk596et1ns7964tlfkg7v3906:/oauth2redirect"
        private const val AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth"
        private const val TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token"
        private const val REQUEST_CODE_AUTH_TOKEN = 42

        const val ACTION_REQUEST_AUTH = "me.cpele.whoami.ACTION_REQUEST_AUTH"
        private const val ACTION_HANDLE_AUTH_RESPONSE = "me.cpele.whoami.ACTION_HANDLE_RESPONSE"
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_REQUEST_AUTH -> requestAuth()
            ACTION_HANDLE_AUTH_RESPONSE -> handleResponse(intent)
        }
    }

    private val authRepository: AuthRepository = CustomApp.INSTANCE.authRepository
    private val authService: AuthorizationService = CustomApp.INSTANCE.authService

    private fun requestAuth() {
        val configuration = AuthorizationServiceConfiguration(Uri.parse(AUTH_ENDPOINT), Uri.parse(TOKEN_ENDPOINT))
        val request = AuthorizationRequest.Builder(
                configuration,
                CLIENT_ID,
                ResponseTypeValues.CODE,
                Uri.parse(REDIRECT_URI)
        ).setScope("profile").build()
        val intent = Intent(application, this::class.java).setAction(ACTION_HANDLE_AUTH_RESPONSE)
        val pendingIntent = PendingIntent.getService(application, REQUEST_CODE_AUTH_TOKEN, intent, 0)
        authService.performAuthorizationRequest(request, pendingIntent)
    }

    private fun handleResponse(intent: Intent) {
        val response = AuthorizationResponse.fromIntent(intent)
        val error = AuthorizationException.fromIntent(intent)
        val authState = AuthState(response, error)
        Log.d(this::class.java.simpleName, "Auth code: ${response?.authorizationCode}")

        authRepository.persist(authState)

        response?.apply {
            authService.performTokenRequest(createTokenExchangeRequest()) { response, ex ->
                authState.update(response, ex)
                authRepository.persist(authState)
            }
        }
    }
}
