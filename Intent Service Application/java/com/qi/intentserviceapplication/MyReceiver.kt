package com.qi.intentserviceapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log

class MyReceiver(val textViewStatus : ShowStatus) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {

        val bundleMessages = intent.extras
        if(bundleMessages != null){
            textViewStatus.messageShow(bundleMessages.getString(DownLoadService.EXTRA_MESSAGE))
            Log.e("ImagevIew"," ${bundleMessages.getParcelable<Uri>("bitmap")}")
        }
    }
}