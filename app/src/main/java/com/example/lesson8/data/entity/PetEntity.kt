package com.example.lesson8.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lesson8.data.MyDbContract

@Entity(tableName = MyDbContract.TablePets.NAME)
data class PetEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String,
)