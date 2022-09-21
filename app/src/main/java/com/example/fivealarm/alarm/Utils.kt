// timeOfAlarm의 값을 Broadcast로 값을 전달해줌.

package com.example.fivealarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent

object Utils {

    fun setAlarm(context: Context, timeOfAlarm: Long) {

        // Intent to start the Broadcast Receiver
        val broadcastIntent = Intent(context
            , NotificationReceiver::class.java)

        val pIntent = PendingIntent.getBroadcast(
            context,
            0,
            broadcastIntent,
            FLAG_IMMUTABLE
        )

        // Setting up AlarmManager
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (System.currentTimeMillis() < timeOfAlarm) {
            alarmMgr.set(
                AlarmManager.RTC_WAKEUP,
                timeOfAlarm,
                pIntent
            )
        }
    }
}