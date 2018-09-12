package me.cpele.whoami

import android.app.PendingIntent
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import me.cpele.whoami.databinding.MainFragmentBinding
import net.openid.appauth.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private const val CLIENT_ID = "511828570984-fuprh0cm7665emlne3rnf9pk34kkn86s.apps.googleusercontent.com"
        private const val REDIRECT_URI = "com.googleusercontent.apps.511828570984-fuprh0cm7665emlne3rnf9pk34kkn86s:/oauth2redirect"
        private const val AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth"
        private const val TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token"
        private const val REQUEST_CODE_AUTH_TOKEN = 42
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

    fun signIn() {
        context?.apply {
            val authorizationService = AuthorizationService(applicationContext)
            val configuration = AuthorizationServiceConfiguration(Uri.parse(AUTH_ENDPOINT), Uri.parse(TOKEN_ENDPOINT))
            val request = AuthorizationRequest.Builder(
                    configuration,
                    CLIENT_ID,
                    ResponseTypeValues.CODE,
                    Uri.parse(REDIRECT_URI)
            ).setScope("profile").build()
            val intent = Intent(applicationContext, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(applicationContext, REQUEST_CODE_AUTH_TOKEN, intent, 0)
            authorizationService.performAuthorizationRequest(request, pendingIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.apply {
            val response = AuthorizationResponse.fromIntent(intent)
            val error = AuthorizationException.fromIntent(intent)
            val authState = AuthState(response, error)
            response?.apply {
                Toast.makeText(applicationContext, "Received auth code: $authorizationCode", Toast.LENGTH_LONG).show()
            }
        }
    }
}
