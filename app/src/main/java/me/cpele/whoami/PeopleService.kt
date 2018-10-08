package me.cpele.whoami

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PeopleService {
    @GET("/plus/v1/people/me")
    fun getMe(@Header("Authorization") authToken: String, @Query("key") key: String): Call<PersonDto>
}
