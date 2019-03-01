package com.baltazar.alarmworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.*
import com.baltazar.alarmworkmanager.util.PreferenceUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val presenter: MainPresenter by inject()

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
        val timeDate = presenter.getTimeLeft()
        text_hour.text = timeDate.hours
        text_minute.text = timeDate.minutes
    }
}
