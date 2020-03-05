package com.qi.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        sendNotification("Simple WorkManager","I have been send by WorkManager")
        return Result.success()
    }

    private fun sendNotification(title : String,message : String) {

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

            val channel =  NotificationChannel("default","Default",NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(applicationContext,"default")
        notification.apply {

            setContentTitle(title)
            setContentText(message)
            setSmallIcon(R.mipmap.ic_launcher)
        }

        notificationManager.notify(1,notification.build())
    }
}