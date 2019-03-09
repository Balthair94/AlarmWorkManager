package com.baltazar.alarmworkmanager.util

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by Baltazar Rodriguez Ramirez on 2/9/19.
 */
class PreferenceUtil(private val context: Context) {

    companion object {
        const val TIME_IN_MINUTES = "time_in_minutes"
        const val TIME_TO_REDUCE = "time_to_reduce"
        const val TIME_WORK_LAST_EXECUTION = "time_work_last_execution"
    }

    fun setMinutes(minutes: Long) {
        getPreferences().edit().apply {
            putLong(TIME_IN_MINUTES, minutes)
        }.apply()
    }

    fun getTimeLeft(): Long = getPreferences().getLong(TIME_IN_MINUTES, 0)

    fun reduceAlarmTime(minutes: Long) {
        val newTime = getTimeLeft().minus(minutes)
        setMinutes(newTime)
    }

    fun setWorkerLasExecution (time: Long) {
        getPreferences().edit().apply{
            putLong(TIME_WORK_LAST_EXECUTION, time)
        }.apply()
    }

    fun getWorkerLastExecution() = getPreferences().getLong(TIME_WORK_LAST_EXECUTION, 0)

    fun setTimeToReduce(minutes: Long): Long {
        getPreferences().edit().apply {
            putLong(TIME_TO_REDUCE, minutes)
        }.apply()
        return minutes
    }

    fun getTimeToReduce(): Long = getPreferences().getLong(TIME_TO_REDUCE, 0)

    fun cleanPreferences() {
        getPreferences().edit().clear().apply()
    }

    private fun getPreferences() = PreferenceManager.getDefaultSharedPreferences(context)
}