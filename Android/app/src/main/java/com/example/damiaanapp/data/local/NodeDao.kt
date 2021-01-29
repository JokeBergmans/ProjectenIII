package com.example.damiaanapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

// Authors: Joke Bergmans, Thibaud Steenhaut
@Dao
interface NodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Node>)

    @Transaction
    @Query("select * from nodes where routeId=:routeId")
    fun getNodesFromRoute(routeId: Int) : LiveData<List<Node>>
}