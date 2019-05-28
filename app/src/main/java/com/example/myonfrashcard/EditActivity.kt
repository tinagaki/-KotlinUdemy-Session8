package com.example.myonfrashcard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import io.realm.Realm
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    lateinit var realm: Realm
    var strQestion: String = ""
    var strAnswer: String = ""
    var intPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        // インテント作成
        val bundle = intent.extras
        val string_status = bundle.getString(getString(R.string.intent_key_status))
        textViewStatus.text = string_status
        // 背景色設定
        constraintLayoutEdit.setBackgroundResource(intBackGroundColor)

        if (string_status == getString(R.string.status_change)) {
            //画面表示の変更
            //strQuestion = bundle.getString(getString(R.string.intent_key_status))

            strQestion = bundle.getString(getString(R.string.intent_key_question)) //mondai
            strAnswer = bundle.getString(getString(R.string.intent_key_answer)) //

            editTextAnwer.setText(strAnswer)
            editTextQuestion2.setText(strQestion)
            intPosition = bundle.getInt(getString(R.string.intent_key_position))

            // 修正の場合は問題が修正できないようにする
            editTextQuestion2.isEnabled = false

        } else {
            editTextQuestion2.isEnabled = true

        }


        // 登録ボタン押したとき
        buttonRegister.setOnClickListener {
            //            新しい単語の追加
            //    既存単語の修正
            if (string_status == getString(R.string.status_add)) {
                addNewWord()
            } else {
                changeWord()
            }

        }
        // 戻るボタン押したとき

        buttonBack2.setOnClickListener { finish() }


    }

    override fun onResume() {
        super.onResume()
        // Realmインスタンス取得
        realm = Realm.getDefaultInstance()


    }

    override fun onPause() {
        super.onPause()
        realm.close()

    }

    private fun addNewWord() {

        // 確認ダイアログ
        val dialog = AlertDialog.Builder(this@EditActivity).apply {
            setTitle("登録")
            setMessage("登録してもいいですか")
            setPositiveButton("はい") { dialog, which ->
                try {
                    //登録
                    realm.beginTransaction()
                    val wordDB = realm.createObject(WordDB::class.java, editTextQuestion2.text.toString())
                    wordDB.strAnswer = editTextAnwer.text.toString()
                    wordDB.boolMemoriFlag = false

                    //   登録完了
                    Toast.makeText(this@EditActivity, "登録が完了しました。", Toast.LENGTH_SHORT).show()

                } catch (e: RealmPrimaryKeyConstraintException) {
                    Toast.makeText(this@EditActivity, "その単語はすでに登録されています。", Toast.LENGTH_SHORT).show()


                }finally {
                    realm.commitTransaction()
                    // クリア
                    editTextQuestion2.setText("")
                    editTextAnwer.setText("")

                }


            }
            setNegativeButton("いいえ") { dialog, which -> }
            show()

        }


    }

    private fun changeWord() {
        // 抽出
        val result = realm.where(WordDB::class.java).findAll().sort(getString(R.string.db_field_question))

        val seledtedDB = result.get(intPosition)

        // 修正ダイヤログ
        val dialog = AlertDialog.Builder(this@EditActivity).apply {
            setTitle(seledtedDB.strAnswer + "の変更")
            setMessage("変更してもいいですか")
            setPositiveButton("はい") { dialog, which ->
                realm.beginTransaction()

                //seledtedDB.strQuestion = editTextQuestion2.text.toString()
                seledtedDB.strAnswer = editTextAnwer.text.toString()
                seledtedDB.boolMemoriFlag = false
                realm.commitTransaction()

                editTextQuestion2.setText("")

                editTextAnwer.setText("")

                Toast.makeText(this@EditActivity, "修正が完了しました。", Toast.LENGTH_SHORT).show()

                finish()

            }
            setNegativeButton("いいえ") { dialog, which ->
            }
            show()
        }


    }
}

