package com.example.other.ttms

import android.app.Application
import android.os.Environment
import com.example.other.ttms.utils.SharedPreferencesUtils



class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPreferencesUtils.init(this)
    }


}