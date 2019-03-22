package com.freeletics.tutorial.mvi

import android.app.Application
import android.content.Context
import com.freeletics.tutorial.mvi.model.ItemDataSource

class MviApplication : Application() {

    val dataSource = ItemDataSource()

    override fun onCreate() {
        super.onCreate()
    }
}

val Context.dataSource
    get() = (applicationContext as MviApplication).dataSource