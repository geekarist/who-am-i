package me.cpele.whoami

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import net.openid.appauth.*

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val CL_ID_PFX: String = BuildConfig.GOOGLE_CLIENT_ID_XFIX
        private const val CLIENT_ID = "$CL_ID_PFX.apps.googleusercontent.com"
        private const val REDIRECT_URI = "com.googleusercontent.apps.$CL_ID_PFX:/oauth2redirect"
        private const val AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth"
        private const val TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token"
        private val AUTH_SCOPES = listOf(
                "https://www.googleapis.com/auth/plus.login",
                "https://www.googleapis.com/auth/plus.me",
                "https://www.googleapis.com/auth/userinfo.email",
                "https://www.googleapis.com/auth/userinfo.profile"
        )
        private const val REQUEST_CODE_AUTH_TOKEN = 42

        const val ACTION_REQUEST_AUTH = "me.cpele.whoami.ACTION_REQUEST_AUTH"
        private const val ACTION_HANDLE_AUTH_RESPONSE = "me.cpele.whoami.ACTION_HANDLE_RESPONSE"
    }

    private val authService by lazy { AuthorizationService(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (intent?.action) {
            ACTION_REQUEST_AUTH -> requestAuth()
            ACTION_HANDLE_AUTH_RESPONSE -> handleResponse(intent)
        }
    }

    private val authHolder: AuthHolder = CustomApp.INSTANCE.authHolder

    private fun requestAuth() {
        val configuration = AuthorizationServiceConfiguration(
                Uri.parse(AUTH_ENDPOINT),
                Uri.parse(TOKEN_ENDPOINT)
        )
        val request = AuthorizationRequest.Builder(
                configuration,
                CLIENT_ID,
                ResponseTypeValues.CODE,
                Uri.parse(REDIRECT_URI)
        ).setScopes(AUTH_SCOPES).build()
        val intent = Intent(application, this::class.java).setAction(ACTION_HANDLE_AUTH_RESPONSE)
        val pendingIntent = PendingIntent.getActivity(
                application,
                REQUEST_CODE_AUTH_TOKEN,
                intent,
                0
        )
        authService.performAuthorizationRequest(request, pendingIntent)
        finish()
    }

    private fun handleResponse(intent: Intent) {
        val response = AuthorizationResponse.fromIntent(intent)
        val error = AuthorizationException.fromIntent(intent)
        val authState = CustomAuthState(response, error)
        Log.d(this::class.java.simpleName, "Auth code: ${response?.authorizationCode}")

        Log.d(
                javaClass.simpleName,
                "Persisting auth state with code: ${authState.jsonSerializeString()}"
        )

        MyAsyncTask(authHolder, authState).execute()

        response?.apply {
            authService.performTokenRequest(createTokenExchangeRequest()) { response, ex ->
                authState.update(response, ex)
                MyAsyncTask(authHolder, authState).execute()
            }
        }
        finish()
    }

    override fun onPause() {
        authService.dispose()
        super.onPause()
    }

    class MyAsyncTask(
            private val authHolder: AuthHolder,
            private val authState: AuthState
    ) : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg p0: Unit?) {
            Log.d(
                    javaClass.simpleName,
                    "Persisting auth state with token: ${authState.jsonSerializeString()}"
            )
            authHolder.persist(authState)
        }
    }
}
