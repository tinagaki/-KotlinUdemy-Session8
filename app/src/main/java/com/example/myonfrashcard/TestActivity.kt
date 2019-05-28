package com.example.myonfrashcard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*
import kotlin.collections.ArrayList

class TestActivity : AppCompatActivity(), View.OnClickListener {

    var boolStatusMemori: Boolean = false
    var intState: Int = 0
    val BEREFORE_START: Int = 1
    val RUNNING_QUESTION: Int = 2
    val RUNNING_ANSWER: Int = 3
    val TEST_FINISHED: Int = 4
    //realm
    lateinit var realm: Realm
    lateinit var results: RealmResults<WordDB>
    lateinit var word_list: ArrayList<WordDB>

    var intCount: Int = 0 //今の問題数

    var intLength: Int = 0
    var boolMemoraized: Boolean = false // 問題を暗記済にするかどうか

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        // テスト条件受け取り
        val bundle = intent.extras
        boolStatusMemori = bundle.getBoolean(getString(R.string.intent_key_memoriy_flag))

        // 背景色の受け取り
        constraintLayoutTest.setBackgroundResource(intBackGroundColor)


        intState = BEREFORE_START
        imageViewFlush.visibility = View.INVISIBLE
        imageViewFlushAnswer.visibility = View.INVISIBLE

        //
        buttonNext.setBackgroundResource(R.drawable.image_button_test_start)
        buttonEndTest.setBackgroundResource(R.drawable.image_button_end_test)



        buttonNext.setOnClickListener(this)
        buttonEndTest.setOnClickListener(this)
        checkBox.setOnClickListener {
            boolMemoraized = checkBox.isChecked
        }

    }

    override fun onResume() {
        super.onResume()
        // DBからデータをもってくる。ランダムに表示する
        realm = Realm.getDefaultInstance()
        // 暗記済のは除外。
        if (boolStatusMemori) {
            results =
                realm.where(WordDB::class.java).equalTo(getString(R.string.db_field_boolMemoriFlag), false).findAll()


        } else {
            results = realm.where(WordDB::class.java).findAll()
        }

        // 問題数の表示
        intLength = results.size
        textViewRemaining.text = intLength.toString()
        // データシャッフル
        word_list = ArrayList(results)
        Collections.shuffle(word_list)

    }

    override fun onPause() {
        super.onPause()
        realm.close()

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonNext ->
                when (intState) {
                    BEREFORE_START -> {
                        // 上のボタン押した際
                        intState = RUNNING_QUESTION
                        showQuestion()
                    }
                    RUNNING_QUESTION -> {
                        // 問題の答え出す
                        intState = RUNNING_ANSWER
                        showAnswer()
                    }
                    // 答えを出したとき
                    RUNNING_ANSWER -> {
                        intState = RUNNING_QUESTION
                        showQuestion()

                    }
                }


            R.id.buttonEndTest -> {
                // 確認ダイヤログ。画面閉じる
                val dialog = AlertDialog.Builder(this@TestActivity).apply {
                    setTitle("テスト修了")
                    setMessage("テストを終了してもいいですか")
                    setPositiveButton("はい") { dialog, which ->
                        if (intState == TEST_FINISHED) {
                            //  最終問題ではいおした瞬間にDB保存
                            val selectDB = realm.where(WordDB::class.java)
                                .equalTo(getString(R.string.db_field_question), word_list[intCount - 1].strQuestion)
                                .findFirst()
                            realm.beginTransaction()
                            selectDB.boolMemoriFlag = boolMemoraized
                            realm.commitTransaction()

                        }
                        finish()
                    }
                    setNegativeButton("いいえ") { dialog, which ->
                    }
                    show()
                }
            }
        }
    }
    private fun showAnswer() {
        imageViewFlushAnswer.visibility = View.VISIBLE
        textViewFlushAnswer.text = word_list[intCount-1].strAnswer
        // 次の問題に進むに変更
        buttonNext.setBackgroundResource(R.drawable.image_button_go_next_question)
        if (intCount == intLength) {
            intState = TEST_FINISHED
            textViewMessage.text = "テスト修了"
            buttonNext.isEnabled = false
            buttonNext.visibility = View.INVISIBLE

            buttonEndTest.setBackgroundResource(R.drawable.image_button_back)
        }


    }

    private fun showQuestion() {
        // 前の問題の暗記済フラグをDB更新

        if (intCount > 0) { // 2問目だけの処理だけの処理
            // 暗記済かどうか
            val selectDB = realm.where(WordDB::class.java)
                .equalTo(getString(R.string.db_field_question), word_list[intCount - 1].strQuestion).findFirst()
            realm.beginTransaction()
            selectDB.boolMemoriFlag = boolMemoraized
            realm.commitTransaction()

        }
        //いまの問題数
        intCount++
        textViewRemaining.text = (intLength - intCount).toString()
//前の表示消す
        imageViewFlushAnswer.visibility = View.INVISIBLE
        textViewFlushAnswer.text = ""
        imageViewFlush.visibility = View.VISIBLE
        textViewFlush.text = word_list[intCount - 1].strQuestion
// 答え見るボタン
        buttonNext.setBackgroundResource(R.drawable.image_button_go_answer)
        // 暗記済みの場合はチェックいれる
        checkBox.isChecked = word_list[intCount - 1].boolMemoriFlag
        boolMemoraized = checkBox.isChecked

    }



}

