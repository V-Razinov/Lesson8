package com.example.lesson8.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lesson8.data.dao.PersonsDao
import com.example.lesson8.data.dao.PetsDao
import com.example.lesson8.data.entity.PersonEntity
import com.example.lesson8.data.entity.PetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PersonEntity::class, PetEntity::class], version = 1, exportSchema = false)
abstract class MyDb : RoomDatabase() {

    abstract fun personDao(): PersonsDao
    abstract fun petsDao(): PetsDao

    companion object {
        private var INSTANCE: MyDb? = null

        fun getInstance(context: Context, scope: CoroutineScope): MyDb {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(
                        context,
                        MyDb::class.java,
                        "my_db"
                    )
                        .addCallback(Callback(scope))
                        .build()
                INSTANCE!!
            }
        }
    }

    private class Callback(
        private val scope: CoroutineScope,
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            INSTANCE?.let { instance ->
                scope.launch(Dispatchers.IO) {
                    val pets = listOf(
                        PetEntity(name = "Кошак"),
                        PetEntity(name = "Собак"),
                    )
                    instance.petsDao().insert(pets)
                    instance.personDao().insertAll(
                        listOf(
                            PersonEntity(
                                name = "Михаил",
                                pet = pets.random()
                            ),
                            PersonEntity(
                                name = "Иван",
                                pet = pets.random()
                            )
                        )
                    )
                }
            }
        }
    }
}