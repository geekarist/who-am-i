package me.cpele.whoami

import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(
                this,
                ProfileViewModel.Factory(AuthRepository(CustomApp.INSTANCE))
        ).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.navigationEvent.observe(this, Observer<LiveEvent<Int>> { resIdEvent ->
            resIdEvent?.run {
                if (isUnconsumed()) {
                    consume()
                    findNavController().navigate(value)
                }
            }
        })
    }
}
