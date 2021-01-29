package com.example.damiaanapp.data

import android.util.Log
import com.example.damiaanapp.util.Resource
import com.example.damiaanapp.util.Resource.Companion.loading
import com.example.damiaanapp.util.Resource.Companion.success
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Authors: Joke Bergmans, Thibaud Steenhaut, Tom Van der Weeën
/**
 * Encapsulate the Retrofit response in a Resource, so that we can catch errors nicely.
 */
abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Call<T>): Resource<T> {
        try {
            val resource: Resource<T>
            val response = call().execute()
            resource = if (response.isSuccessful)
                success(response.body()!!)
            else
                error("${response.message()}: + ${response.errorBody()}")
            return resource

        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        Log.e("remoteDataSource", message)
        return Resource.error(data = null, message = "Network call has failed for a following reason: $message")
    }
}