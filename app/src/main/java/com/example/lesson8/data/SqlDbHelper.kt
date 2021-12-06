package com.example.lesson8.data

import android.provider.BaseColumns
import android.provider.BaseColumns._ID

object MyDbContract {

    object TablePersons : BaseColumns {
        const val NAME = "persons"
        const val COLUMN_NAME = "name"
        const val COLUMN_PET = "pet_id"
        const val COLUMN_AGE = "age"

        const val QUERY_CREATE_TABLE = "CREATE TABLE $NAME (" +
                "$_ID INTEGER PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_AGE INTEGER" +
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