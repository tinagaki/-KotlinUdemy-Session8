package com.example.myonfrashcard

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_word_list.*

class WordListActivity : AppCompatActivity(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    lateinit var realm: Realm
    lateinit var results: RealmResults<WordDB>
    lateinit var word_list: ArrayList<String>

    lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)

        // 画面開いたとき

        // 前の背景色の引き継ぎ
        constraintLayoutEdit.setBackgroundResource(intBackGroundColor)


        //新しい単語の追加ボタン
        buttonAddNewWord.setOnClickListener {

            val intent = Intent(this@WordListActivity, EditActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_status), getString(R.string.status_add))
            startActivity(intent)
        }
        // 戻るボタン押したとき

        buttonBack.setOnClickListener {
            finish()
        }

        // 暗記済は下にぼたんを押したとき
        buttonSort.setOnClickListener {
            word_list.clear()
            results =
                realm.where<WordDB>(WordDB::class.java).findAll().sort(getString(R.string.db_field_boolMemoriFlag))
            results.forEach {
                if (it.boolMemoriFlag) {
                    word_list.add(it.strAnswer + ":" + it.strQuestion + " [暗記済]")

                } else {
                    word_list.add(it.strAnswer + ":" + it.strQuestion)

                }
            }
            listView.adapter = adapter

        }



        listView.setOnItemClickListener(this)
        listView.setOnItemLongClickListener(this)


    }

    override fun onResume() {
        super.onResume()
        realm = Realm.getDefaultInstance()
        // データ取得
        results =
            realm.where(WordDB::class.java).findAll().sort(getString(R.string.db_field_answer))
        val length = results.size
        word_list = ArrayList<String>()
        // for 文でリストの表示形式をカエル
//        for (i in 0..length-1 ) {
//            if(results[i].boolMemoriFlag){
//
//                word_list.add(results[i].strAnswer + ":" + results[i].strQuestion+" [暗記済]")
//            }else{
//            word_list.add(results[i].strAnswer + ":" + results[i].strQuestion)
//    }
//        }

        results.forEach {
            if (it.boolMemoriFlag) {
                word_list.add(it.strAnswer + ":" + it.strQuestion + " [暗記済]")

            } else {
                word_list.add(it.strAnswer + ":" + it.strQuestion)

            }
        }


        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, word_list)
        // listviewの表示
        listView.adapter = adapter


    }

    override fun onPause() {
        super.onPause()
        realm.close()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //DBから取得
        val selectedDB = results[position]
        val seledtecQuestion = selectedDB.strQuestion
        val selectedAnswer = selectedDB.strAnswer
        val intent = Intent(this@WordListActivity, EditActivity::class.java).apply {
            putExtra(getString(R.string.intent_key_question), seledtecQuestion)
            putExtra(getString(R.string.intent_key_answer), selectedAnswer)
            putExtra(getString(R.string.intent_key_position), position)
            putExtra(getString(R.string.intent_key_status), getString(R.string.status_change))


        }

//        intent.putExtra(getString(R.string.intent_key_question), seledtecQuestion)
//        intent.putExtra(getString(R.string.intent_key_answer), selectedAnswer)
//        intent.putExtra(getString(R.string.intent_key_position), position)
//        intent.putExtra(getString(R.string.intent_key_status), getString(R.string.status_change))


        startActivity(intent)

    }

    // 長押し 削除
    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        val selectedDB = results[position]

        // 確認ダイヤログ追加

        val dialog = AlertDialog.Builder(this@WordListActivity).apply {
            this.setTitle(selectedDB.strAnswer + "の削除")
            setMessage("削除してもいいですか")
            setPositiveButton("はい") { dialog, which ->
                val seledtecQuestion = selectedDB.strQuestion
                val selectedAnswer = selectedDB.strAnswer
                realm.beginTransaction()
                selectedDB.deleteFromRealm()
                realm.commitTransaction()
                // 画面の削除
                word_list.removeAt(position)
                // アダプターの方針
                listView.adapter = adapter

            }
            setNegativeButton("いいえ") {
                dialog, which ->

            }
            show()
        }



        return true

    }


}
