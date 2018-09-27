package me.cpele.whoami

import retrofit2.Call
import retrofit2.http.GET

interface PeopleService {
    @GET("/plus/v1/people/userId/me")
    fun getMe(): Call<PersonBo>
}
