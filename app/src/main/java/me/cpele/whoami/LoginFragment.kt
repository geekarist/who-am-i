package me.cpele.whoami


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import me.cpele.whoami.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(
                this,
                LoginViewModel.Factory(AuthHolder(CustomApp.INSTANCE), CustomApp.INSTANCE)
        ).get(LoginViewModel::class.java)

        viewModel.loginEvent.observe(this, Observer { event ->
            event?.value?.let {
                findNavController().popBackStack()
                event.consume()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
    }
}
