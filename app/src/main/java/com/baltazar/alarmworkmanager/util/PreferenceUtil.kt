package com.baltazar.alarmworkmanager.util

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by Baltazar Rodriguez Ramirez on 2/9/19.
 */
class PreferenceUtil(private val context: Context) {

    companion object {
        const val ALARM_TIME = "alarm_time"
    }

    fun getAlarmTime(): Long {
        return getPreferences().getLong(ALARM_TIME, 0)
    }

    fun setAlarmTime(time: Long) {
        getPreferences().edit().apply {
            putLong(ALARM_TIME, time)
        }.apply()
    }

    fun reduceAlarmTime() {
        val newTime = getAlarmTime().minus(1)
        setAlarmTime(newTime)
    }

    private fun getPreferences() = PreferenceManager.getDefaultSharedPreferences(context)
}