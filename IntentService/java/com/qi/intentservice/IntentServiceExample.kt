package com.qi.intentservice

import android.app.IntentService
import android.content.Intent
import android.os.SystemClock
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class IntentServiceExample(name: String) : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {

        val messageToString = intent?.getStringExtra("message")
        SystemClock.sleep(4000)
        val echoMessage = "Intent service pause after 3 sec"+messageToString

        intent?.putExtra("BroadcastMessage",echoMessage)?.let {
            LocalBroadcastManager.getInstance(this).sendBroadcast(
                it
            )
        }
    }
}
