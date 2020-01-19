package com.blursutdio.airwatch

import android.util.Log
import java.net.URL

class request(private val url: String) {

    fun run() {
        val responseJson = URL(url).readText()
        Log.d(javaClass.simpleName, responseJson)
    }
}