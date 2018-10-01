package me.cpele.whoami

import android.app.Application
import android.arch.lifecycle.*
import android.util.Log
import android.view.View
import com.google.gson.Gson

class ProfileViewModel(
        application: Application,
        authHolder: AuthHolder,
        personRepository: PersonRepository
) : AndroidViewModel(application) {

    val navigationEvent: LiveData<LiveEvent<Int>> =
            Transformations.map(authHolder.state) { state ->
                if (true == state?.isAuthorized) {
                    Log.d(this::class.java.simpleName, "Auth holder state is authorized")
                    null
                } else {
                    Log.d(this::class.java.simpleName, "Auth holder state is not authorized")
                    LiveEvent(R.id.navigate_to_login)
                }
            }

    private val personRespData: LiveData<Resource<PersonBo>> =
            Transformations.switchMap(authHolder.state) { state ->
                state?.accessToken?.let { token ->
                    personRepository.findOneByToken(token)
                }
            }

    val name: LiveData<String> =
            Transformations.map(personRespData) { resp ->
                Log.d(javaClass.simpleName, "Response: ${Gson().toJson(resp)}")
                resp.value?.name?.formatted
            }

    val nameVisibility: LiveData<Int> = Transformations.map(personRespData) { resp ->
        if (resp.value?.name?.formatted == null) View.GONE else View.VISIBLE
    }

    val error: LiveData<String> = Transformations.map(personRespData) { resp ->
        Log.d(javaClass.simpleName, "Response: ${Gson().toJson(resp)}")
        resp.error?.message
    }

    val errorVisibility: LiveData<Int> = Transformations.map(personRespData) { resp ->
        if (resp.error?.message != null) View.VISIBLE else View.GONE
    }

    class Factory(
            private val application: Application,
            private val authHolder: AuthHolder,
            private val personRepository: PersonRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(ProfileViewModel(
                    application,
                    authHolder,
                    personRepository
            )) as T
        }
    }
}
