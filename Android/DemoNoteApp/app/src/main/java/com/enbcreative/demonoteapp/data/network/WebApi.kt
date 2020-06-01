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
    @POST("api.php?enbapicall=login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("api.php?enbapicall=signup")
    suspend fun signup(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("gender") gender: String? = "male"
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("api.php?enbapicall=notes")
    suspend fun getNotes(@Field("userId") userId: Int): Response<NotesResponse>

    @FormUrlEncoded
    @POST("api.php?enbapicall=insertnote")
    suspend fun insertNewNote(
        @Field("userId") userId: Int,
        @Field("content") content: String,
        @Field("created_at") created_at: String
    ): Response<NotesResponse>

    @FormUrlEncoded
    @POST("api.php?enbapicall=updatenote")
    suspend fun updateNote(
        @Field("id") id: Int,
        @Field("userId") userId: Int,
        @Field("content") content: String,
        @Field("updated_at") updated_at: String
    ): Response<NotesResponse>

    @FormUrlEncoded
    @POST("api.php?enbapicall=deletenote")
    suspend fun deleteNote(
        @Field("id") id: Int,
        @Field("userId") userId: Int
    ): Response<NotesResponse>

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