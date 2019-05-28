package com.example.myonfrashcard

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * TODO クラス説明
 *
 * Created by tomohiroinagaki on 2019-05-27.
 */
class MyAoocation: Application()
{
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)


    }
}