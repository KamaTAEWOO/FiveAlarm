package com.example.fivealarm.alarm

import android.app.TimePickerDialog
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.fivealarm.R
import com.example.fivealarm.UserInfo
import kotlinx.android.synthetic.main.activity_alarm.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.text.DateFormat


class Alarm : AppCompatActivity() {
    private val TAG: String = "Alarm"

    private var txt_alarmCount: TextView? = null
    private var txt_inTime: TextView? = null
    private var txt_outTime: TextView? = null
    private var txt_leaveTime: TextView? = null
    private var btn_nextTime: AppCompatButton? = null
    private var btn_reset: ImageView? = null

    var timeInMilliSeconds: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        allFindViewById()

        // 출근 시간
        txt_inTime!!.text = UserInfo._workInTime

        // 퇴근 시간
        txt_outTime!!.text = UserInfo._workOutTime

        // count
        txt_alarmCount!!.text = UserInfo._hourCount.toString()
        
        // 퇴근시간 이용해서 남은 시간을 구해보기
        var token = UserInfo._workInTime!!.split("")
        leaveTime(token)

        //timeInMilliSeconds = 0
        val receiver = ComponentName(applicationContext, BootCompleteReceiver::class.java)

        applicationContext.packageManager?.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        // Start Time 클릭 시 Time Picker 나타남.
        startTimeText.setOnClickListener {
            // Get Current Time
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minuteOfHour ->

                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minuteOfHour)
                    calendar.set(Calendar.SECOND, 0)

                    val amPm = if (hourOfDay < 12) "am" else "pm"
                    val formattedTime = String.format("%02d:%02d %s", hourOfDay, minuteOfHour, amPm)
                    startTimeText.text = formattedTime

                    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
                    val formattedDate = sdf.format(calendar.time)
                    val date = sdf.parse(formattedDate)
                    timeInMilliSeconds = date.time
                }, hour, minute, false
            )
            timePickerDialog.show()
        }

        // Set Alarm Button Click
        setAlarm.setOnClickListener {
            if (timeInMilliSeconds.toInt() != 0) {
                Toast.makeText(this, "Alarm has been set!", Toast.LENGTH_LONG)
                    .show() // timeInMilliSeconds의 값이 0이 아니면

                // Shared Preference에 값 저장
                val sharedPref = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                    ?: return@setOnClickListener
                with(sharedPref.edit()) {
                    putLong("timeInMilli", timeInMilliSeconds)
                    apply()
                }
                Utils.setAlarm(this, timeInMilliSeconds)
            } else {
                Toast.makeText(this, "Please enter the time first!", Toast.LENGTH_LONG)
                    .show() // timeInMilliSeconds의 값이 0이면
            }
        }
    }
    
    // 퇴근 시간을 이용해서 남은 시간 구하기
    private fun leaveTime(token: List<String>) {
        val inHour: String = token[2] + token[3]
        val inMinute: String = token[7] + token[8]
        Log.d(TAG, "leaveTime() - $token, $inHour, $inMinute")

        calTime(inHour.toInt(), inMinute.toInt())
        Log.d(TAG, "leaveTime() timeInMilliSeconds - $timeInMilliSeconds")
    }

    private fun calTime(hourOfDay: Int, minuteOfHour: Int) {
        // Get Current Time
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minuteOfHour)
        calendar.set(Calendar.SECOND, 0)

        val amPm = if (hourOfDay < 12) "am" else "pm"
        val formattedTime = String.format("%02d:%02d %s", hourOfDay, minuteOfHour, amPm)
        startTimeText.text = formattedTime

        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val formattedDate = sdf.format(calendar.time)
        val date = sdf.parse(formattedDate)
        timeInMilliSeconds = date.time
        val time: String = convertSecondsToHMmSs(timeInMilliSeconds)
        Log.d(TAG, "calTime() - $time,   $sdf")
    }

    fun convertSecondsToHMmSs(seconds: Long): String {
        val formatter: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"))
        val text: String = formatter.format(Date(seconds))
        return text
    }

    private fun allFindViewById() {
        txt_alarmCount = findViewById(R.id.txt_alarmCount)
        txt_inTime = findViewById(R.id.txt_in_time)
        txt_outTime = findViewById(R.id.txt_out_time)
        txt_leaveTime = findViewById(R.id.txt_leave_time)
        btn_nextTime = findViewById(R.id.btn_nextTime)
        btn_reset = findViewById(R.id.btn_reset)
    }
}