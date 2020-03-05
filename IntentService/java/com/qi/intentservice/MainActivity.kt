package com.qi.intentservice

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),ShowTextMessage {

    private var edittextView : EditText? = null
    private var sendButton : ImageButton? = null

    var FILTER_ANY_KEY = "any_keys"
    private lateinit var myServices: MyServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendButton = findViewById(R.id.imageButton)
        edittextView = findViewById(R.id.editText)

        val messageToString = edittextView?.text?.toString()

        sendButton?.setOnClickListener {
            val intentService = Intent(this,MyServices :: class.java)
            intentService.apply {
                putExtra("message",messageToString)
            }
            startService(intentService)
        }
    }
    fun sentReceiver(){

        myServices = MyServices(this)

        val intentFilter = IntentFilter().apply {
            addAction(FILTER_ANY_KEY)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(myServices,intentFilter)
    }

    override fun onStart() {
        super.onStart()
        sentReceiver()
    }

    override fun showTextMessage(message: String?) {
        textView.text = message.toString()
    }
}

interface ShowTextMessage {
    fun showTextMessage(message : String?)
}
