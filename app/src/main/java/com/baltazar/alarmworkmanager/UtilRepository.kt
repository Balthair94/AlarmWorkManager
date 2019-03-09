package com.baltazar.alarmworkmanager

import android.content.Context
import android.widget.Toast
import androidx.work.WorkManager
import com.baltazar.alarmworkmanager.util.PreferenceUtil
import com.baltazar.alarmworkmanager.util.TimeData
import java.util.Date

/**
 * Created by Baltazar Rodriguez Ramirez on 2/28/19.
 */
interface UtilRepository {

    fun getTimeLeft(): TimeData?

    fun getPreferences(): PreferenceUtil

    fun validateWorkManagerState(): Int
}

class UtilRepositoryImpl(private val mContext: Context, private val mPreferences: PreferenceUtil = PreferenceUtil(mContext)): UtilRepository {

    override fun getTimeLeft(): TimeData? {
        val currentDate = Date(System.currentTimeMillis()).time
        val timeLeft = mPreferences.getTimeLeft()
        val workerLastExecutionDate = mPreferences.getWorkerLastExecution()
        val timeToReduce = mPreferences.getTimeToReduce()

        if (timeToReduce > 0) {

            val diff = getMinutesDifference(currentDate, workerLastExecutionDate)

            val minutesToNextExecution = timeToReduce - diff

            val newTimeLeft = timeLeft + minutesToNextExecution

            val hours = newTimeLeft.div(60)
            val minutes = newTimeLeft.rem(60)

            return TimeData(hours, minutes)
        }

        return null
    }

    override fun getPreferences() = mPreferences

    override fun validateWorkManagerState(): Int {
        val timeToReduce = mPreferences.getTimeLeft()

        if (timeToReduce > 0) {
            WorkManager.getInstance().getWorkInfosByTag(AlarmWorker.TAG).get()?.let { statusList ->
                val isComplete = statusList.all { it.state.isFinished }
                return if (isComplete) AlarmWorker.ALARM_STATE_RUNNIG else AlarmWorker.ALARM_STATE_STOP
            }
        }

        return AlarmWorker.ALARM_STATE_STOP
    }

    private fun getMinutesDifference(dateNew: Long, dateOld: Long): Long {
        val diff = dateNew - dateOld
        return (diff / (60 * 1000)).rem(60)
    }

}