package com.example.fastcampus_3

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    //onCreate 시점에 View가 그려지기 때문에 그 전에는 lazy하게 선언하는 것이다
    private val numberPicker1 : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val numberPicker2 : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val numberPicker3 : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3).apply {
            minValue = 0
            maxValue = 9
        }
    }

     private val openButton : AppCompatButton by lazy{
         findViewById(R.id.openButton)
     }

    private val changePasswordButton : AppCompatButton by lazy{
        findViewById(R.id.changePasswordButton)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //by lazy때문에 적어둠
        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener {

            if(changePasswordMode){
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPreference = getSharedPreferences("password", Context.MODE_PRIVATE) //다른 앱과 공유하지 않고 이 앱에서만 사용하는 모드

            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            //getString으로 password 폴더 안에 password라는 이름으로 파일을 만듬 , 기본값은 000
            if(passwordPreference.getString("password", "000").equals(passwordFromUser)){
                //패스워드 성공

                //TODO 로 해야 할 일 표시 가능함
                startActivity(Intent(this, DiaryActivity::class.java))
            } else{
                //패스워드 실패
                showErrorAlertDialog()
            }
        }

        changePasswordButton.setOnClickListener {

            val passwordPreference = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if(changePasswordMode){ //번호를 저장하는 기능

                //commit은 다 저장이 될 때 까지 UI를 멈추고 기다리는 기능
                //apply는 바로 저장하라고 넘겨주고 다른 작업을 실행 (비동기적)
                /*
                passwordPreference.edit {
                    val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"
                    putString("password", passwordFromUser)
                    commit()
                }*/

                //kotlin-extension을 통해서 commit이나 apply를 안하는 일을 방지해줌
                passwordPreference.edit(true){
                    putString("password", passwordFromUser)
                }

                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)

            } else{ //changePasswordMode가 활성화 :: 비밀번호가 맞는지를 체크

                if(passwordPreference.getString("password", "000").equals(passwordFromUser)){
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()

                    changePasswordButton.setBackgroundColor(Color.RED)

                } else{
                    //패스워드 실패
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog(){
        AlertDialog.Builder(this)
            .setTitle("실패!")
            .setMessage("비밀번호가 잘못되었습니다")
            .setPositiveButton("닫기"){ _, _ ->
                numberPicker1.value = 0
                numberPicker2.value = 0
                numberPicker3.value = 0
            }
            .create()
            .show()
    }
}