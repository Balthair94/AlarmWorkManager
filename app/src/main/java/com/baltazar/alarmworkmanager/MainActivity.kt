package com.baltazar.alarmworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.*
import com.baltazar.alarmworkmanager.util.PreferenceUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Date
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setClickListener()
    }

    override fun onResume() {
        super.onResume()
        validateWorkerState()
        updateTimeUi()
    }

    private fun setClickListener() {
        button_start.setOnClickListener {
            stopWorker()

            val preferenceUtil = PreferenceUtil(this)
            val timeToReduce = preferenceUtil.setTimeToReduce(15)
            preferenceUtil.setMinutes(timeToReduce)

            val data = Data.Builder().apply {
                putLong(AlarmWorker.TIME_TO_REDUCE, 15)
            }.build()

            val request = PeriodicWorkRequest.Builder(AlarmWorker::class.java, timeToReduce, TimeUnit.MINUTES)
                .setInputData(data)
                .addTag(AlarmWorker.TAG)
                .build()

            WorkManager.getInstance().enqueue(request)

            validateWorkerState()
        }

        button_reset.setOnClickListener {
            stopWorker()
            text_hour.text = "00"
            text_minute.text = "15"
        }
    }

    private fun validateWorkerState() {
        val preferences = PreferenceUtil(this)
        val leftTime = preferences.getTimeLeft()
        val timeToReduce = preferences.getTimeToReduce()

        if (timeToReduce > 0) {
            Toast.makeText(this, "Left time $leftTime", Toast.LENGTH_SHORT).show()

            WorkManager.getInstance().getWorkInfosByTag(AlarmWorker.TAG).get()?.let { statusList ->
                var isComplete = false
                if (statusList.isNotEmpty()) {
                    isComplete = statusList.all { it.state.isFinished }
                }

                val message = if (isComplete) "No workers running" else "Worker is running"

                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        } else {
            stopWorker()
        }
    }

    private fun stopWorker() {
        WorkManager.getInstance().run {
            cancelAllWorkByTag(AlarmWorker.TAG)
        }
    }

    private fun updateTimeUi() {
        val preferenceUtil = PreferenceUtil(this)
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

            text_hour.text = if (hours <= 9) "0$hours" else "$hours"
            text_minute.text = if (minutes <= 9) "0$minutes" else "$minutes"
        }
    }

    private fun getMinutesDifference(dateNew: Long, dateOld: Long): Long {
        val diff = dateNew - dateOld
        return (diff / (60 * 1000)).rem(60)
    }
}
