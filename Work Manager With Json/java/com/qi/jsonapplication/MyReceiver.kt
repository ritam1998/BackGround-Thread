package com.qi.jsonapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver(val showAllJsonData : SHOWALLJSONDATA) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val bundleMessage = intent?.action
        if(bundleMessage.equals(WorkWithJson.ACTION_SEND)){
            val jsonString = intent?.extras?.getString("JSONARRAY")
            showAllJsonData.showAllData(jsonString)
        }
    }
}