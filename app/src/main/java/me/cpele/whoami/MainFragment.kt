package me.cpele.whoami

import android.app.Application
import android.app.PendingIntent
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import me.cpele.whoami.databinding.MainFragmentBinding
import net.openid.appauth.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private const val CLIENT_ID = "709489431311-7fkp3nqqk596et1ns7964tlfkg7v3906.apps.googleusercontent.com"
        private const val REDIRECT_URI = "com.googleusercontent.apps.709489431311-7fkp3nqqk596et1ns7964tlfkg7v3906:/oauth2redirect"
        private const val AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth"
        private const val TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token"
        private const val REQUEST_CODE_AUTH_TOKEN = 42
        private const val ACTION_AUTH_TOKEN_RESPONSE = "me.cpele.whoami.AUTH_TOKEN_RESPONSE"
    }

    private lateinit var viewModel: MainViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = MainFragmentBinding.bind(view)
        binding.viewModel = viewModel
        binding.controller = this
        binding.setLifecycleOwner(this)
    }

    private val authService by lazy { context?.applicationContext?.let { AuthorizationService(it) } }

    fun signIn() {
        context?.apply {
            val configuration = AuthorizationServiceConfiguration(Uri.parse(AUTH_ENDPOINT), Uri.parse(TOKEN_ENDPOINT))
            val request = AuthorizationRequest.Builder(
                    configuration,
                    CLIENT_ID,
                    ResponseTypeValues.CODE,
                    Uri.parse(REDIRECT_URI)
            ).setScope("profile").build()
            val intent = Intent(applicationContext, MainActivity::class.java).setAction(ACTION_AUTH_TOKEN_RESPONSE)
            val pendingIntent = PendingIntent.getActivity(applicationContext, REQUEST_CODE_AUTH_TOKEN, intent, 0)
            authService?.performAuthorizationRequest(request, pendingIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.apply {
            if (intent.action == ACTION_AUTH_TOKEN_RESPONSE) {

                val response = AuthorizationResponse.fromIntent(intent)
                val error = AuthorizationException.fromIntent(intent)
                val authState = AuthState(response, error)
                Log.d(MainFragment::class.java.simpleName, "Auth code: ${response?.authorizationCode}")

                authState.apply {
                    persistTo(applicationContext)
                    if (authorizationException != null) {
                        Toast.makeText(
                                applicationContext,
                                "Auth error: ${authorizationException?.message}",
                                Toast.LENGTH_LONG
                        ).show()
                        Log.w(MainFragment::class.java.simpleName, authorizationException)
                    } else {
                        response?.apply {
                            authService?.performTokenRequest(createTokenExchangeRequest()) { response, ex ->
                                update(response, ex)
                                persistTo(applicationContext)
                                authService?.let { service ->
                                    performActionWithFreshTokens(service) { accessToken, _, _ ->
                                        accessToken?.let {
                                            ProfileAsyncTask(applicationContext as? Application).execute(it)
                                        }
                                    }
                                }
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
