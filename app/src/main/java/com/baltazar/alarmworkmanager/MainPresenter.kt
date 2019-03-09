package com.baltazar.alarmworkmanager

import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Created by Baltazar Rodriguez Ramirez on 2/28/19.
 */
class MainPresenter(private val mRepository: UtilRepository): MainContract.Presenter {

    private var mView: MainContract.View? = null

    override fun getTimeLeft() {
        mRepository.getTimeLeft()?.let {
            mView?.updateTimeUi(it.hours.toString(), it.minutes.toString())
        }
    }

    override fun onAttach(view: Any) {
        mView = view as MainContract.View

        validateWorkerState()
    }

    override fun startAlarm(minutes: Long) {
        val preferenceUtil = mRepository.getPreferences()
        val timeToReduce = preferenceUtil.setTimeToReduce(minutes)
        preferenceUtil.setMinutes(timeToReduce)

        val data = Data.Builder().apply {
            putLong(AlarmWorker.TIME_TO_REDUCE, timeToReduce)
        }.build()

        val request = PeriodicWorkRequest.Builder(AlarmWorker::class.java, timeToReduce, TimeUnit.MINUTES)
            .setInputData(data)
            .addTag(AlarmWorker.TAG)
            .build()

        WorkManager.getInstance().enqueue(request)

        validateWorkerState()
    }

    override fun stopAlarm() {
        WorkManager.getInstance().run {
            cancelAllWorkByTag(AlarmWorker.TAG)
        }
        mView?.updateTimeUi("00", "00")
    }

    override fun validateWorkerState() {
        val message = when(mRepository.validateWorkManagerState()) {
            AlarmWorker.ALARM_STATE_RUNNIG -> "Worker is running"
            AlarmWorker.ALARM_STATE_STOP -> "No workers running"
            else -> "Invalid state"
        }

        mView?.showMessage(message)
    }
}