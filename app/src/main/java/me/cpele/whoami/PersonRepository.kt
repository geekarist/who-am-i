package me.cpele.whoami

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

class PersonRepository {
    fun findOneByToken(token: String): LiveData<PersonBo> {
        return MutableLiveData<PersonBo>().apply { value = PersonBo(NameBo("Person with token: $token")) }
    }
}
