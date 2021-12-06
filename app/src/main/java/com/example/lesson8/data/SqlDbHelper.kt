package com.example.lesson8

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.provider.BaseColumns._ID
import com.example.lesson8.data.PersonEntity
import com.example.lesson8.data.PetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object MyDbContract {

    object TablePersons : BaseColumns {
        const val NAME = "persons"
        const val COLUMN_NAME = "name"
        const val COLUMN_PET = "pet_id"

        const val QUERY_CREATE_TABLE = "CREATE TABLE $NAME (" +
                "$_ID INTEGER PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_PET INTEGER)"
    }

    object TablePets : BaseColumns {
        const val NAME = "pets"
        const val COLUMN_NAME = "name"

        const val QUERY_CREATE_TABLE = "CREATE TABLE $NAME (" +
                "$_ID INTEGER PRIMARY KEY," +
                "$COLUMN_NAME TEXT)"
    }
}

class SqlDbHelper(context: Context) : SQLiteOpenHelper(context, db_name, null, db_version) {

    companion object {
        const val db_version = 1
        const val db_name = "my_db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db ?: return
        db.execSQL(MyDbContract.TablePersons.QUERY_CREATE_TABLE)
        db.execSQL(MyDbContract.TablePets.QUERY_CREATE_TABLE)

        val pets = listOf(
            PetEntity(id = 0, name = "Кошак"),
            PetEntity(id = 1, name = "Собак")
        )
        val persons = listOf(
            PersonEntity(id = 0, name = "Михаил", pet = pets.random()),
            PersonEntity(id = 1, name = "Иван", pet = pets.random()),
            PersonEntity(id = 2, name = "Петр", pet = pets.random())
        )
        GlobalScope.launch(Dispatchers.Main) {
            insertPets(db, pets)
            insertPersons(db, persons)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    private fun insertPersons(
        db: SQLiteDatabase,
        persons: List<PersonEntity>,
    ) {
        val values = ContentValues()
        persons.forEach { person ->
            values.apply {
                put(BaseColumns._ID, person.id)
                put(MyDbContract.TablePersons.COLUMN_NAME, person.name)
                put(MyDbContract.TablePersons.COLUMN_PET, person.pet.id)
            }
            db.insert(MyDbContract.TablePersons.NAME, null, values)
        }
    }

    private fun insertPets(
        db: SQLiteDatabase,
        pets: List<PetEntity>,
    ) {
        val values = ContentValues()
        pets.forEach { pet ->
            values.apply {
                put(BaseColumns._ID, pet.id)
                put(MyDbContract.TablePets.COLUMN_NAME, pet.name)
            }
            db.insert(MyDbContract.TablePets.NAME, null, values)
        }
    }
}