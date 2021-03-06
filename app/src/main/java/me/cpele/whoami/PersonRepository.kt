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

    fun findOneByToken(token: String): LiveData<ResourceDto<PersonDto>> {

        val result = MutableLiveData<ResourceDto<PersonDto>>()

        val peopleService = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(PeopleService::class.java)

        peopleService.getMe(
                "Bearer $token",
                BuildConfig.GOOGLE_API_KEY
        ).enqueue(object : Callback<PersonDto?> {
            override fun onFailure(call: Call<PersonDto?>, t: Throwable) {
                TODO("Set value to Resource<PersonBo> with failure = Throwable")
            }

            override fun onResponse(call: Call<PersonDto?>, response: Response<PersonDto?>) {
                response.apply {
                    result.value =
                            if (isSuccessful) {
                                val resource: ResourceDto<PersonDto> = ResourceDto(value = body())
                                Log.d(javaClass.simpleName, resource.toString())
                                resource
                            }
                            else {
                                val errorStr = errorBody()?.string()
                                Log.d(javaClass.simpleName, errorStr)
                                ResourceDto(error = gson.fromJson(errorStr, RespErrorDto::class.java))
                            }
                }
            }
        })

        return result
    }
}

