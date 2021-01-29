package com.example.damiaanapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

//Author: Tom Van der Weeën
@Dao
interface ParticipantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(participant: Participant)

    @Update
    fun updateParticipant(participant: Participant)

    @Transaction
    @Query("SELECT * FROM participants WHERE id=:id")
    fun getParticipant(id: Int): LiveData<Participant>

    @Transaction
    @Query("SELECT * FROM participants LIMIT 1")
    fun getParticipant(): LiveData<Participant>

    @Transaction
    @Query("DELETE FROM participants")
    fun clear()

}