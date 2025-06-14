package com.example.currencyconverter

import android.app.Application
import android.content.Context
import com.example.currencyconverter.data.dataSource.room.ConverterDatabase

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        initDatabases(applicationContext)
    }

    private fun initDatabases(context: Context) {
        ConverterDatabase.create(context)
    }
}