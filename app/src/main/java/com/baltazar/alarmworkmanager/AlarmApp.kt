package com.baltazar.alarmworkmanager

import android.app.Application
import com.baltazar.alarmworkmanager.util.getModule
import org.koin.android.ext.android.startKoin

/**
 * Created by Baltazar Rodriguez Ramirez on 2/28/19.
 */
class AlarmApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(getModule(this)))
    }
}