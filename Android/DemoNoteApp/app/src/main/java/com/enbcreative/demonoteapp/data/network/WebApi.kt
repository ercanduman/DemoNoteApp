package com.enbcreative.demonoteapp.data.network

import com.enbcreative.demonoteapp.BASE_API_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface WebApi {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("signup")
    suspend fun signup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ApiResponse>

    /**
     * URL: BASE_API_URL + "notes?userId=1"
     * Ex: http://dustsite.blogspot.com/notes?userId=1
     */
    @GET("notes")
    suspend fun getNotes(@Query("userId") userId: Int): Response<NotesResponse>

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