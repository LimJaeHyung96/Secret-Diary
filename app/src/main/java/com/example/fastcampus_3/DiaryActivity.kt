package com.example.fastcampus_3

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity:AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper()) //메인 쓰레드에 연결된 핸들러가 만들어진다

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiry_diary)

        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val detailPreference = getSharedPreferences("diary", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreference.getString("detail",""))

        val runnable = Runnable {
            //수시로 백그라운드에서 저장해야 하므로 commit을 쓰지 않고 apply를 사용함
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit{
                putString("detail", diaryEditText.text.toString())
            }
        }

        //내용이 수정될 때 마다 저장 , 0.5초 동안 텍스트 변화가 없으면 runnable이 실행되는 것
        diaryEditText.addTextChangedListener {
            //새로운 쓰레드와 UI 쓰레드를 연결해주는 기능을 Handler를 이용해서 자주 함
            handler.removeCallbacks(runnable) //0.5초마다 실행될 때 아직 실행되지 않고 pending(보류) 되어있는 runnable을 제거함
            handler.postDelayed(runnable, 500)
        }
    }
}