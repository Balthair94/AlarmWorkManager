package com.baltazar.alarmworkmanager.extension

import android.content.Context
import android.widget.Toast

/**
 * Created by Baltazar Rodriguez Ramirez on 3/2/19.
 */

fun Context.showToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}