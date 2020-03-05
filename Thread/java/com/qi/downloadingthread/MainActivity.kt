package com.qi.downloadingthread

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity(),ShowImageMessage {

    private var downloadButton : ImageButton? = null
    private var urlText : EditText? = null
    private var progressBar : ProgressBar? = null

    private lateinit var downLoadFile : DownLoadFile
    private lateinit var messageHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlText = findViewById(R.id.urltextview)
        downloadButton = findViewById(R.id.imageButton)
        progressBar = findViewById(R.id.progressBar)

        messageHandler = Handler(Looper.getMainLooper())

        downloadButton?.setOnClickListener {

            Log.e("Url","URL Link : ${urlText?.text?.toString()}")
            Toast.makeText(this,"${urlText?.text?.toString()}",Toast.LENGTH_LONG).show()

            Log.e("network Strength"," ${checkInternetConnection()}")
            progressBar?.visibility = View.VISIBLE
            downLoadFile = DownLoadFile(urlString = urlText?.text?.toString(),showImageMessage = this,handler = messageHandler)
            downLoadFile.start()
        }
    }

    private fun checkInternetConnection() : Boolean{


        var result = false
        /* Get Connectivity Manager Object to check connection */
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        /* Check For Network Connections */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val networkCapabilities = connectivityManager.activeNetwork ?: return false

            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            result = when {

                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        return result
    }

    override fun handleMessage(msg: Message) {

        Log.e("Message "," ${msg.data.getParcelable<Bitmap>("bitmap")}")
        val imageView : ImageView = findViewById(R.id.imageView)
        if(imageView != null){
            imageView.setImageBitmap(msg.data.getParcelable<Bitmap>("bitmap"))
            progressBar?.visibility = View.GONE
        }else{
            progressBar?.visibility = View.VISIBLE
        }
    }
}
interface ShowImageMessage {
    fun handleMessage(msg : Message)
}
