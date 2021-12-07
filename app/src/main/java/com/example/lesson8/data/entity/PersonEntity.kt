package com.example.lesson8.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lesson8.data.MyDbContract

@Entity(tableName = MyDbContract.TablePersons.NAME)
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String,
    val age: Int,
    @Embedded(prefix = "pet")
    val pet: PetEntity
)