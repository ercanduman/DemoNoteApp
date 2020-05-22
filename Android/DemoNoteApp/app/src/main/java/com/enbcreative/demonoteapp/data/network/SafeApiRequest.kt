package com.enbcreative.demonoteapp.data.network

import com.enbcreative.demonoteapp.utils.ApiException
import com.enbcreative.demonoteapp.utils.logd
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.toString()
            val stringBuilder = StringBuilder()
            error?.let {
                try {
                    val message = JSONObject(it).getString(ApiResponse.JSON_FIELD_MESSAGE)
                    stringBuilder.append(message)
                } catch (e: JSONException) {
                    logd(e.message.toString())
                }
                stringBuilder.append("\n")
            }
            val errorCode = "Error Code: ${response.code()}"
            stringBuilder.append(errorCode)
            throw ApiException(stringBuilder.toString())
        }
    }
}