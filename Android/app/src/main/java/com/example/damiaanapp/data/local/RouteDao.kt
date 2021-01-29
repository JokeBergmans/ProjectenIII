package com.example.damiaanapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

// Authors: Joke Bergmans, Thibaud Steenhaut, Tom Van der Weeën
@Dao
interface RouteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Route>)

    @Transaction
    @Query("select * from routes where id=:id")
    fun getRoute(id: Int) : LiveData<Route>

    @Transaction
    @Query("Select * from routes")
    fun getRoutes() : LiveData<List<Route>>
}