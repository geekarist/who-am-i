package me.cpele.whoami

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    private val _navigationEvent =
            MutableLiveData<LiveEvent<Int>>().apply { value = LiveEvent(R.id.navigate_to_login) }
    val navigationEvent: LiveData<LiveEvent<Int>> = _navigationEvent
}
