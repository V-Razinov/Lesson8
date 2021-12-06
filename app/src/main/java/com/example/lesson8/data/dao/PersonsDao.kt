package com.example.lesson8.data.dao

import androidx.room.*
import com.example.lesson8.data.MyDbContract
import com.example.lesson8.data.entity.PersonEntity

@Dao
interface PersonsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<PersonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: PersonEntity)

    @Query("SELECT * FROM ${MyDbContract.TablePersons.NAME}")
    fun readAll() : List<PersonEntity>

    @Delete
    fun delete(personEntity: PersonEntity)
}