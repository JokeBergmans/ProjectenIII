package com.example.damiaanapp.data.remote

import com.example.damiaanapp.model.ParticipantModel
import com.example.damiaanapp.model.RegistrationModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*



interface DamiaanService {


    @POST("Account")
    @Headers("Content-Type: application/json")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @PUT("Participant/{id}")
    @Headers ("Content-Type: application/json")
    fun updatePreferences(@Path("id") id: Int, @Body participant: ParticipantModel): Call<ParticipantModel>

    @POST("Routes/{id}/Registrations/{registrationId}/Scanned")
    @Headers("Content-Type: application/json")
    fun addScanned(@Path("id") routeId: Int, @Path("registrationId") registrationId: Int, @Query("nodeId") nodeId: Int): Call<Void>

    @GET("Participant/{id}/Registrations")
    @Headers("Content-Type: application/json")
    fun getRegistrations(@Path("id") id : Int): Call<List<RegistrationModel>>

    @GET("Participant/{id}")
    @Headers("Content-Type: application/json")
    fun getParticipant(@Path("id") id : Int): Call<ParticipantModel>
}



