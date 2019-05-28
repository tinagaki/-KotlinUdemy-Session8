package com.example.myonfrashcard

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

var intBackGroundColor = 0;


class MainActivity : AppCompatActivity() {


    // 色ボタンを押したとき

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // 編集ボタン
        buttonEdit.setOnClickListener {
            val intent = Intent(this@MainActivity, WordListActivity::class.java)
            startActivity(intent)
        }

        // 色ボタンを押したとき
        buttonColor01.setOnClickListener {
            intBackGroundColor = R.color.color01
            ConstraintLayoutMain.setBackgroundResource(intBackGroundColor)

        }
        buttonColor02.setOnClickListener {
            intBackGroundColor = R.color.color02
            ConstraintLayoutMain.setBackgroundResource(intBackGroundColor)

        }
        buttonColor03.setOnClickListener {
            intBackGroundColor = R.color.color03
            ConstraintLayoutMain.setBackgroundResource(intBackGroundColor)

        }
        buttonColor04.setOnClickListener {
            intBackGroundColor = R.color.color04
            ConstraintLayoutMain.setBackgroundResource(intBackGroundColor)

        }
        buttonColor05.setOnClickListener {
            intBackGroundColor = R.color.color05
            ConstraintLayoutMain.setBackgroundResource(intBackGroundColor)

        }
        buttonColor06.setOnClickListener {
            intBackGroundColor = R.color.color06
            ConstraintLayoutMain.setBackgroundResource(intBackGroundColor)

        }
        // テストクリック時
        buttonTest.setOnClickListener {
            val intent = Intent(this@MainActivity,TestActivity::class.java)


            //radioGroup.setOnCheckedChangeListener{}

            when(radioGroup.checkedRadioButtonId){
                // 暗記済
                R.id.radioButton-> intent.putExtra(getString(R.string.intent_key_memoriy_flag),true)
                    R.id.radioButton2->intent.putExtra(getString(R.string.intent_key_memoriy_flag),false)
            }
            startActivity(intent)

        }


    }


}
