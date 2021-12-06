package com.example.lesson8.data

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import androidx.core.database.sqlite.transaction
import com.example.lesson8.MyDbContract
import com.example.lesson8.SqlDbHelper

class DbRepository(context: Context) {

    private val db = SqlDbHelper(context)

    fun insertPersons(vararg persons: PersonEntity) {
        db.writableDatabase.transaction {
            val values = ContentValues()
            persons.forEach { person ->
                values.put(MyDbContract.TablePersons.COLUMN_NAME, person.name)
                values.put(MyDbContract.TablePersons.COLUMN_PET, person.pet.id)
                insert(MyDbContract.TablePersons.NAME, null, values)
            }
        }
    }

    fun updatePerson(person: PersonEntity) {
        db.writableDatabase.transaction {
            val values = ContentValues().apply {
                put(MyDbContract.TablePersons.COLUMN_NAME, person.name)
                put(MyDbContract.TablePersons.COLUMN_PET, person.id)
            }
            update(
                MyDbContract.TablePersons.NAME,
                values,
                "${BaseColumns._ID} = ?",
                arrayOf(person.id.toString())
            )
        }
    }

    fun deletePerson(person: PersonEntity) {
        db.writableDatabase.transaction {
            delete(
                MyDbContract.TablePersons.NAME,
                "${BaseColumns._ID} = ?",
                arrayOf(person.id.toString())
            )
        }
    }

    fun readPersons(): List<PersonEntity> {
        val personId = "person_id"
        val personName = "person_name"
        val petId = "pet_id"
        val petName = "pet_name"
        return db.readableDatabase.rawQuery(
            "SELECT " +
                    "${MyDbContract.TablePersons.NAME}.${BaseColumns._ID} as $personId, " +
                    "${MyDbContract.TablePersons.NAME}.${MyDbContract.TablePersons.COLUMN_NAME} as $personName, " +
                    "${MyDbContract.TablePets.NAME}.${BaseColumns._ID} as $petId, " +
                    "${MyDbContract.TablePets.NAME}.${MyDbContract.TablePets.COLUMN_NAME} as $petName " +
                "FROM ${MyDbContract.TablePersons.NAME} " +
                "INNER JOIN ${MyDbContract.TablePets.NAME} " +
                "ON ${MyDbContract.TablePersons.NAME}.${MyDbContract.TablePersons.COLUMN_PET} = ${MyDbContract.TablePets.NAME}.${BaseColumns._ID}",
            null
        ).use { cursor ->
            val persons = mutableListOf<PersonEntity>()
            if (cursor.moveToFirst()) {
                do {
                    persons.add(
                        PersonEntity(
                            id = cursor.getInt(cursor.getColumnIndexOrThrow(personId)),
                            name = cursor.getString(cursor.getColumnIndexOrThrow(personName)),
                            pet = PetEntity(
                                id = cursor.getInt(cursor.getColumnIndexOrThrow(petId)),
                                name = cursor.getString(cursor.getColumnIndexOrThrow(petName))
                            )
                        )
                    )
                } while (cursor.moveToNext())
            }
            persons
        }
    }

    fun insertPets(pets: List<PetEntity>) {
        db.writableDatabase
            .transaction {
                val values = ContentValues()
                pets.forEach { person ->
                    values.put(MyDbContract.TablePets.COLUMN_NAME, person.name)
                    insert(MyDbContract.TablePets.NAME, null, values)
                }
            }
    }

    fun readPets(): List<PetEntity> {
        val projection = arrayOf(BaseColumns._ID, MyDbContract.TablePets.COLUMN_NAME)
        db.readableDatabase.query(
            MyDbContract.TablePets.NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        ).use { cursor ->
            val pets = mutableListOf<PetEntity>()
            if (cursor.moveToFirst()) {
                do {
                    pets.add(
                        PetEntity(
                            id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)),
                            name = cursor.getString(cursor.getColumnIndexOrThrow(MyDbContract.TablePets.COLUMN_NAME))
                        )
                    )
                } while (cursor.moveToNext())
            }
            return pets.toList()
        }
    }
}

data class PersonEntity(
    val id: Int = -1,
    val name: String,
    val pet: PetEntity
)

data class PetEntity(
    val id: Int = -1,
    val name: String
)