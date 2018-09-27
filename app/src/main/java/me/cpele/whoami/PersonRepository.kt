package me.cpele.whoami

import android.arch.lifecycle.MutableLiveData

class PersonRepository {
    val one = MutableLiveData<PersonBo>().apply { value = PersonBo(NameBo("Unknown person")) }
}
