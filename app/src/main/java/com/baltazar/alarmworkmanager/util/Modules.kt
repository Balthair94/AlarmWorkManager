package com.baltazar.alarmworkmanager.util

import android.content.Context
import com.baltazar.alarmworkmanager.MainPresenter
import com.baltazar.alarmworkmanager.UtilRepository
import com.baltazar.alarmworkmanager.UtilRepositoryImpl
import org.koin.dsl.module.module

/**
 * Created by Baltazar Rodriguez Ramirez on 2/28/19.
 *
 * TODO QUE ES UN dsl
 */

fun getModule(context: Context) = module {
    single<UtilRepository> { UtilRepositoryImpl(context) }
    factory { MainPresenter(get()) }
}