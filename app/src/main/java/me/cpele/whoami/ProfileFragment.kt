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
import me.cpele.whoami.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProviders.of(
                this,
                CustomApp.INSTANCE.profileViewModelFactory
        ).get(ProfileViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        viewModel.navigationEvent.observe(this, Observer<LiveEvent<Int>> { event ->
            event?.value?.let { action ->
                findNavController().navigate(action)
                event.consume()
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
}
