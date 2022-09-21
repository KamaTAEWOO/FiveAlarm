package com.example.fivealarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.widget.AppCompatButton
import com.example.fivealarm.alarm.Alarm
import com.google.android.material.button.MaterialButtonToggleGroup


class UserInfo : AppCompatActivity() {
    private val TAG: String = "UserInfo"

    // Toggle 버튼
    private var toggle_inGroup: MaterialButtonToggleGroup? = null
    private var toggle_outGroup: MaterialButtonToggleGroup? = null
    private var toggle_inAM: Button? = null
    private var toggle_inPM: Button? = null
    private var toggle_outAM: Button? = null
    private var toggle_outPM: Button? = null

    // 출근 시간 & 퇴근 시간
    private var et_inHour: EditText? = null
    private var et_inMinute: EditText? = null
    private var txt_inTime: TextView? = null
    private var btn_inSet: AppCompatButton? = null

    private var et_outHour: EditText? = null
    private var et_outMinute: EditText? = null
    private var txt_outTime: TextView? = null
    private var btn_outSet: AppCompatButton? = null
    
    // start 버튼
    private var btn_start: AppCompatButton? = null

    // 오전 / 오후
    var INAMPM: String? = ""
    var OUTAMPM: String? = ""

    // count를 위한 inHour
    var inHourNum: Int = 0
    var outHourNum: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        allFindViewById()

        // TODO 클릭 중복 오류 있음. 참고
        toggle_inGroup!!.addOnButtonCheckedListener{ toggle_inGroup, checkedId, isChecked ->
            if(isChecked) {
                Log.d(TAG, "::toggle  toggle index PM")
                toggleCheck(1)
            } else {
                Log.d(TAG, "::toggle  toggle index AM")
                toggleCheck(0)
            }
        }

        // TODO 클릭 중복 오류 있음. 참고
        toggle_outGroup!!.addOnButtonCheckedListener{ toggle_inGroup, checkedId, isChecked ->
            if(isChecked) {
                Log.d(TAG, "::toggle  toggle index PM")
                toggleCheck(3)
            } else {
                Log.d(TAG, "::toggle  toggle index AM")
                toggleCheck(2)
            }
        }

        // 출근 시간 설정 완료 버튼
        btn_inSet!!.setOnClickListener{
            workTimeInCheck()
        }
        // 퇴근 시간 설정 완료 버튼
        btn_outSet!!.setOnClickListener{
            workTimeOutCheck()
        }
        // Start 버튼
        btn_start!!.setOnClickListener{
            print()
            _hourCount = outHourNum - inHourNum
            startActivity(Intent(baseContext, Alarm::class.java))
        }
    }

    private fun allFindViewById() {
        toggle_inGroup = findViewById(R.id.toggle_InGroup)
        toggle_outGroup = findViewById(R.id.toggle_OutGroup)
        toggle_inAM = findViewById(R.id.toggle_in_am)
        toggle_inPM = findViewById(R.id.toggle_in_pm)
        toggle_outAM = findViewById(R.id.toggle_out_am)
        toggle_outPM = findViewById(R.id.toggle_out_pm)

        et_inHour = findViewById(R.id.et_in_hour)
        et_inMinute = findViewById(R.id.et_in_minute)
        et_outHour = findViewById(R.id.et_out_hour)
        et_outMinute = findViewById(R.id.et_out_minute)

        txt_inTime = findViewById(R.id.txt_in_time)
        txt_outTime = findViewById(R.id.txt_out_time)

        btn_inSet = findViewById(R.id.btn_in_set)
        btn_outSet = findViewById(R.id.btn_out_set)
        btn_start = findViewById(R.id.btn_start)
    }

    // AM, PM 값을 구해야함.
    private fun toggleCheck(num: Int) {
        // 0 -> AM ,  1 -> PM
        INAMPM = if(num == 0) {
            "AM"
        } else {
            "PM"
        }

        OUTAMPM = if(num == 2) {
            "AM"
        } else {
            "PM"
        }
    }

    // User가 쓴 출근 시간을 가지고 와서 정리
    private fun workTimeInCheck() {
        // 출근 시간 09시 00분
        val inHour: String = et_inHour!!.text.toString()
        val inMinute: String = et_inMinute!!.text.toString()
        inHourNum = inHour.toInt()
        Log.d(TAG, "::workTimeInCheck  inHour: $inHour, inMinute: $inMinute")
        txt_inTime!!.text = "$INAMPM $inHour 시 $inMinute 분"
        _workInTime = "$INAMPM $inHour 시 $inMinute 분"
    }
    // User가 쓴 퇴근 시간을 가지고 와서 정리
    private fun workTimeOutCheck() {
        // 퇴근 시간 18시 00분
        val outHour: String = et_outHour!!.text.toString()
        val outMinute: String = et_outMinute!!.text.toString()
        outHourNum = outHour.toInt()
        Log.d(TAG, "::workTimeOutCheck  inHour: $outHour, inMinute: $outMinute")
        txt_outTime!!.text = "$OUTAMPM $outHour 시 $outMinute 분"
        _workOutTime = "$OUTAMPM $outHour 시 $outMinute 분"
    }

    // 전체 출력 및 확인용 함수
    private fun print() {
        Log.d(TAG, "::print()  _INAMPM: $INAMPM, _OUTAMPM: $OUTAMPM, _workInTime: $_workInTime, _workOutTime: $_workOutTime ")
    }

    companion object {
        var _workInTime: String? = ""
        var _workOutTime: String? = ""
        var _hourCount: Int = 0
    }
}