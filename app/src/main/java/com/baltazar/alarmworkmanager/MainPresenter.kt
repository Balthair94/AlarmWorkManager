package com.baltazar.alarmworkmanager

/**
 * Created by Baltazar Rodriguez Ramirez on 2/28/19.
 */
class MainPresenter(val repo: UtilRepository) {

    fun getTimeLeft() = repo.getTimeLeft()
}