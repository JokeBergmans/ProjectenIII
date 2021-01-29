package com.example.damiaanapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

// Authors: Joke Bergmans, Thibaud Steenhaut
@Dao
interface RegistrationDao {
    @Transaction
    @Query("select * from registrations")
    fun getAllRegistrations(): LiveData<List<RegistrationAndRoute>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Registration>)

    @Transaction
    @Query("select * from registrations inner join routes r where r.id=:routeId")
    fun getRegistrationByRouteId(routeId: Int): LiveData<RegistrationAndRoute>
}