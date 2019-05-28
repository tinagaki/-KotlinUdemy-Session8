package com.example.myonfrashcard

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * モデルクラスの作成
 *
 * Created by tomohiroinagaki on 2019-05-27.
 */

open class WordDB : RealmObject() {
    //主キー
    //問題
    @PrimaryKey
    open var strQuestion: String = ""
    // 答え
    open var strAnswer: String = ""
    // 暗記済
    open var boolMemoriFlag: Boolean = false


}