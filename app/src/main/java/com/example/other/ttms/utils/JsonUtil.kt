package com.example.other.ttms.utils

import android.util.Log
import com.example.other.ttms.beans.ResultInfo
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.util.ArrayList


object JsonUtil {
    fun parseData(data: String?): String? {
        if (data != null) {
            val gson = Gson()
            val resultInfo = gson.fromJson(data, ResultInfo::class.java)
            if (resultInfo != null) {
                Log.e("resultInfo", resultInfo.result + "------" + resultInfo.msg)

                if (resultInfo.result.equals("success") && resultInfo.msg == 1) {
                    return resultInfo.data
                }
            }
        }
        return null
    }

    fun <T> parseArray(s: String, classOfT: Class<T>): MutableList<T> {
        val result = ArrayList<T>()
        val parser = JsonParser()
        val jsonElements = parser.parse(s).asJsonArray
        val gson = Gson()
        for (e in jsonElements) {
            val info = gson.fromJson(e, classOfT)
            result.add(info)
        }
        return result
    }
}