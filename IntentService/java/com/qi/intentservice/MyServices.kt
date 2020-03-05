package com.qi.intentservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyServices(val textMessage : ShowTextMessage) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        var getMessage = intent?.getStringExtra("BroadcastMessage")
        textMessage.showTextMessage(getMessage)
    }
}

