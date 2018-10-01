package me.cpele.whoami

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonRepository(private val gson: Gson) {

    fun findOneByToken(token: String): LiveData<Resource<PersonBo>> {

        val result = MutableLiveData<Resource<PersonBo>>()

        val peopleService = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(PeopleService::class.java)

        peopleService.getMe(
                token,
                "AIzaSyBOmTwHDWBiRnIF9-ByRJy6ed3ZXUA9wLQ"
        ).enqueue(object : Callback<PersonBo?> {
            override fun onFailure(call: Call<PersonBo?>, t: Throwable) {
                TODO("Set value to Resource<PersonBo> with failure = Throwable")
            }

            override fun onResponse(call: Call<PersonBo?>, response: Response<PersonBo?>) {
                response.apply {
                    result.value =
                            if (isSuccessful) Resource(value = body())
                            else {
                                val errorStr = errorBody()?.string()
                                Log.d(javaClass.simpleName, errorStr)
                                Resource(error = gson.fromJson(errorStr, RespError::class.java))
                            }
                }
            }
        })

        return result
    }
}

