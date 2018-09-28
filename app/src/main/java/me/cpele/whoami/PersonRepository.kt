package me.cpele.whoami

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonRepository {

    fun findOneByToken(token: String): LiveData<PersonBo> {

        val result = MutableLiveData<PersonBo>()

        val peopleService = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PeopleService::class.java)

        peopleService.getMe(
                token,
                "AIzaSyBOmTwHDWBiRnIF9-ByRJy6ed3ZXUA9wLQ"
        ).enqueue(object : Callback<PersonBo?> {
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
