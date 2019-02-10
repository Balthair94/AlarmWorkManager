package com.baltazar.alarmworkmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.baltazar.alarmworkmanager.util.PreferenceUtil

/**
 * Created by Baltazar Rodriguez Ramirez on 2/9/19.
 */
class AlarmWorker(context: Context, params: WorkerParameters): Worker(context, params) {

    companion object {
        const val TAG = "AlarmWorker"
    }

    override fun doWork(): Result {
        val timeInSeconds = inputData.getLong(PreferenceUtil.ALARM_TIME, 0)

        return try {
            for (i in 0 until timeInSeconds) {
                PreferenceUtil(applicationContext).reduceAlarmTime()
                Thread.sleep(1_000)
            }
            Result.success()
        } catch (error: Throwable) {
            Result.failure()
        }
    }
}