package com.baltazar.alarmworkmanager

/**
 * Created by Baltazar Rodriguez Ramirez on 3/2/19.
 */
interface MainContract {

    interface View {

        fun updateTimeUi(hour: String, minutes: String)

        fun showMessage(message: String)

    }

    interface Presenter {

        fun onAttach(view: Any)

        fun startAlarm(minutes: Long)

        fun stopAlarm()

        fun validateWorkerState()

        fun getTimeLeft()
    }
}