package com.baltazar.alarmworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.*
import com.baltazar.alarmworkmanager.extension.showToastMessage
import com.baltazar.alarmworkmanager.util.PreferenceUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MainContract.View {

    private val mPresenter: MainPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setClickListener()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onAttach(this)
        mPresenter.getTimeLeft()
    }

    override fun updateTimeUi(hour: String, minutes: String) {
        text_hour.text = hour
        text_minute.text = minutes
    }

    override fun showMessage(message: String) {
        showToastMessage(message)
    }

    private fun setClickListener() {
        button_start.setOnClickListener {
            mPresenter.stopAlarm()
            mPresenter.startAlarm(30)
        }

        button_reset.setOnClickListener { mPresenter.stopAlarm() }
    }
}
