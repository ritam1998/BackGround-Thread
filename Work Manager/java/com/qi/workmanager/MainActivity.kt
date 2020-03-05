package com.qi.workmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WorkManagerButton.setOnClickListener {
            val simpleRequest = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
            WorkManager.getInstance(this).enqueue(simpleRequest)
        }
    }
}
