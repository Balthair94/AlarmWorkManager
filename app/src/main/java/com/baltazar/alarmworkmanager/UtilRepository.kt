package com.baltazar.alarmworkmanager

import android.content.Context
import com.baltazar.alarmworkmanager.util.PreferenceUtil
import com.baltazar.alarmworkmanager.util.TimeData
import java.util.*

/**
 * Created by Baltazar Rodriguez Ramirez on 2/28/19.
 */
interface UtilRepository {
    fun getTimeLeft(): TimeData
}

class UtilRepositoryImpl(private val mContext: Context): UtilRepository {

    override fun getTimeLeft(): TimeData {
        val preferenceUtil = PreferenceUtil(mContext)
        val currentDate = Date(System.currentTimeMillis()).time
        val timeLeft = preferenceUtil.getTimeLeft()
        val workerLastExecutionDate = preferenceUtil.getWorkerLastExecution()
        val timeToReduce = preferenceUtil.getTimeToReduce()

        if (timeToReduce > 0) {

            val diff = getMinutesDifference(currentDate, workerLastExecutionDate)

            val minutesToNextExecution = timeToReduce - diff

            val newTimeLeft = timeLeft + minutesToNextExecution

            val hours = newTimeLeft.div(60)
            val minutes = newTimeLeft.rem(60)

            val hour = if (hours <= 9) "0$hours" else "$hours"
            val minute = if (minutes <= 9) "0$minutes" else "$minutes"

            return TimeData(hour, minute)
        }

        return TimeData("0", "0")
    }

    private fun getMinutesDifference(dateNew: Long, dateOld: Long): Long {
        val diff = dateNew - dateOld
        return (diff / (60 * 1000)).rem(60)
    }

}