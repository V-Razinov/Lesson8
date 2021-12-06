package com.example.lesson8

import android.app.Application
import com.example.lesson8.data.DbRepository

class App : Application() {

    lateinit var dbRepository: DbRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        dbRepository = DbRepository(this)
    }

    companion object {
        lateinit var instance: App
    }
}