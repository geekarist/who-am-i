package me.cpele.whoami

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.BindingAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import me.cpele.whoami.databinding.FragmentProfileBinding
import net.openid.appauth.AuthorizationService

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProviders.of(
                this,
                CustomApp.INSTANCE.profileViewModelFactory
        ).get(ProfileViewModel::class.java)
    }

    private val authService: AuthorizationService? by lazy { activity?.let { AuthorizationService(it) } }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        viewModel.navigationEvent.observe(this, Observer<LiveEvent<Int>> { event ->
            Log.d(this::class.java.simpleName, "Navigation event is: ${event?.value}")
            event?.value?.let { action ->
                findNavController().navigate(action)
                event.consume()
            }
        })

        viewModel.authState.observe(this, Observer { state ->
            authService?.let { authServ ->
                state?.apply { needsTokenRefresh = true }
                        ?.performActionWithFreshTokens(authServ) { accessToken, _, ex ->
                            viewModel.freshTokenData.value = Pair(accessToken, ex)
                        }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
    }

    override fun onDestroy() {
        authService?.dispose()
        super.onDestroy()
    }
}

@BindingAdapter("imageUrl")
fun setImgUrl(view: ImageView, url: String?) {
    view.context?.let { Glide.with(it).load(url).into(view) }
}
