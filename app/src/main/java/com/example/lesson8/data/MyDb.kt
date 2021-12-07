package com.example.lesson8.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lesson8.data.dao.PersonsDao
import com.example.lesson8.data.dao.PetsDao
import com.example.lesson8.data.entity.PersonEntity
import com.example.lesson8.data.entity.PetEntity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

@Database(
    entities = [PersonEntity::class, PetEntity::class],
    version = 2,
    exportSchema = true
)
abstract class MyDb : RoomDatabase() {

    abstract fun personDao(): PersonsDao
    abstract fun petsDao(): PetsDao

    companion object {
        private var INSTANCE: MyDb? = null
        var callbackOnCreateJob: Job? = null

        fun getInstance(context: Context, scope: CoroutineScope): MyDb {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(
                        context,
                        MyDb::class.java,
                        "my_db"
                    )
                        .addCallback(Callback(scope))
                        .addMigrations(Migration1to2())
                        .build()
                INSTANCE!!
            }
        }
    }

    private class Migration1to2 : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE ${MyDbContract.TablePersons.NAME} " +      //NOT NULL для "Int" иначе будет "Int?"
                        "ADD COLUMN ${MyDbContract.TablePersons.COLUMN_AGE} INTEGER NOT NULL DEFAULT 0"
            )
        }
    }

    private class Callback(
        private val scope: CoroutineScope,
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            INSTANCE?.let { instance ->
                callbackOnCreateJob = scope.launch(Dispatchers.IO) {
                    val pets = listOf(
                        PetEntity(name = "Кошак"),
                        PetEntity(name = "Собак"),
                    )
                    instance.petsDao().insert(pets)
                    instance.personDao().insertAll(
                        listOf(
                            PersonEntity(
                                name = "Михаил",
                                age = Random.nextInt(18, 100),
                                pet = pets.random()
                            ),
                            PersonEntity(
                                name = "Иван",
                                age = Random.nextInt(18, 100),
                                pet = pets.random()
                            )
                        )
                    )
                }
            }
        }
    }
}