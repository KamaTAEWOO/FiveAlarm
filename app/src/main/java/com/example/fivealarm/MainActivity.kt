package com.example.fivealarm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.fivealarm.alarm.Alarm

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({ startActivity(Intent(baseContext, UserInfo::class.java)) }, 3000L)
    }
}
