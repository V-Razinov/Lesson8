package com.example.lesson8.data

import android.database.sqlite.SQLiteDatabase

class DbMigrationHelper {

    fun upgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2)
            migrate1to2(db)
        if (oldVersion < 3)
            migrate2to3(db)
    }

    fun downgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    private fun migrate1to2(db: SQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE ${MyDbContract.TablePersons.NAME} " +
                    "ADD COLUMN ${MyDbContract.TablePersons.COLUMN_AGE} " + "INTEGER DEFAULT -1"
        )
    }

    private fun migrate2to3(sqLiteDatabase: SQLiteDatabase) {

    }
}