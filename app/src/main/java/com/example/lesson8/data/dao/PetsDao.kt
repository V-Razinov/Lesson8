package com.example.lesson8.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lesson8.data.MyDbContract
import com.example.lesson8.data.entity.PetEntity

@Dao
interface PetsDao {

    @Query("SELECT * FROM ${MyDbContract.TablePets.NAME}")
    fun readAll(): List<PetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pets: List<PetEntity>)
}