package com.baltazar.alarmworkmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.baltazar.alarmworkmanager.util.PreferenceUtil
import com.baltazar.alarmworkmanager.util.makeStatusNotification
import java.util.Date

/**
 * Created by Baltazar Rodriguez Ramirez on 2/9/19.
 */
class AlarmWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object {
        const val TAG = "AlarmWorker"
        const val TIME_TO_REDUCE = "time_to_reduce"

        const val ALARM_STATE_RUNNING = 0
        const val ALARM_STATE_STOP = 1
    }

    override fun doWork(): Result {
        val preferenceUtil = PreferenceUtil(applicationContext)
        val currentMinutes = preferenceUtil.getTimeLeft()
        val timeToReduce = inputData.getLong(TIME_TO_REDUCE, 0)

        return try {
            if (currentMinutes <= 0) {
                preferenceUtil.cleanPreferences()
                makeStatusNotification("Time is over, you have to take your medicine", applicationContext)
            } else {
                val date = Date(System.currentTimeMillis())
                preferenceUtil.setWorkerLasExecution(date.time)
                preferenceUtil.reduceAlarmTime(timeToReduce)
            }
            Result.success()
        } catch (error: Throwable) {
            Result.failure()
        }
    }
}