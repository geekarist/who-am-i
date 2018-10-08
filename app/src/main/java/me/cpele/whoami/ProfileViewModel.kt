package me.cpele.whoami

import android.app.Application
import android.arch.lifecycle.*
import android.util.Log
import android.view.View

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

    private val personRespData: LiveData<ResourceDto<PersonDto>> =
            Transformations.switchMap(authHolder.state) { state ->
                state?.accessToken?.let { token ->
                    personRepository.findOneByToken(token)
                }
            }

    val name: LiveData<String> =
            Transformations.map(personRespData) { resp ->
                "${resp.value?.name?.givenName} ${resp.value?.name?.familyName}"
            }

    val nameVisibility: LiveData<Int> = Transformations.map(personRespData) { resp ->
        if (resp.value?.name?.givenName == null) View.GONE else View.VISIBLE
    }

    val error: LiveData<String> = Transformations.map(personRespData) { resp ->
        "An error has occurred: code: [${resp.error?.error?.code}], message: [${resp.error?.error?.message}]"
    }

    val errorVisibility: LiveData<Int> = Transformations.map(personRespData) { resp ->
        if (resp.error?.error?.code != null) View.VISIBLE else View.GONE
    }

    val imgUrl: LiveData<String> = Transformations.map(personRespData) {
        it.value?.image?.url?.replace("?sz=50", "?sz=400")
    }

    val email: LiveData<String> = Transformations.map(personRespData) {
        it.value?.emails?.get(0)?.value
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
