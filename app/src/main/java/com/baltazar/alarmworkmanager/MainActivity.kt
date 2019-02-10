package com.baltazar.alarmworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.baltazar.alarmworkmanager.util.PreferenceUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setClickListener()
    }

    override fun onResume() {
        super.onResume()
        validateWorkerState()
    }

    private fun setClickListener() {
        button_start.setOnClickListener {
            val data = Data.Builder().apply {
                putLong(PreferenceUtil.ALARM_TIME, 50)
            }.build()

            val request = OneTimeWorkRequestBuilder<AlarmWorker>()
                .setInputData(data)
                .addTag(AlarmWorker.TAG)
                .build()

            WorkManager.getInstance()
                .beginWith(request)
                .enqueue()

            validateWorkerState()
        }

        button_reset.setOnClickListener {
            WorkManager.getInstance().run {
                cancelAllWorkByTag(AlarmWorker.TAG)
                validateWorkerState()
            }
        }
    }

    private fun validateWorkerState() {
        WorkManager.getInstance().getWorkInfosByTag(AlarmWorker.TAG).get()?.let { statusList ->
            var isComplete = false
            if (statusList.isNotEmpty()) {
                isComplete = statusList.all { it.state.isFinished }
            }

            val message = if (isComplete) "No workers running" else "Worker is running"

            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}
