package com.enbcreative.demonoteapp.data.network

import com.enbcreative.demonoteapp.BASE_API_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface WebApi {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ApiResponse>

    companion object {
        operator fun invoke(): WebApi {
            return Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebApi::class.java)
        }
    }
}