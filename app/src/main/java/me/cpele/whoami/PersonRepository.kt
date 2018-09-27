package me.cpele.whoami

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class PersonRepository {
    fun findOneByToken(token: String): LiveData<PersonBo> {

        val result = MutableLiveData<PersonBo>()

        val retrofit = Retrofit.Builder().baseUrl("www.googleapis.com").build()

        val peopleService = retrofit.create(PeopleService::class.java)

        peopleService.getMe().enqueue(object : Callback<PersonBo?> {
            override fun onFailure(call: Call<PersonBo?>, t: Throwable) {
                TODO("not implemented")
            }

            override fun onResponse(call: Call<PersonBo?>, response: Response<PersonBo?>) {
                response.apply {
                    if (isSuccessful) result.value = body()
                    else TODO()
                }
            }
        })

        return result
    }
}
