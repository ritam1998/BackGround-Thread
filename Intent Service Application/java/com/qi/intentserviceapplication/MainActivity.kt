package com.qi.intentserviceapplication

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileInputStream
import java.util.*


class MainActivity : AppCompatActivity(),ShowStatus,ShowImage {

    private var imageview : ImageView? = null
    private var imageButton : ImageButton? = null
    private var enterText : EditText? = null

    private lateinit var messageHandler : Handler
    private lateinit var downLoadService: DownLoadService


    private var dowmloadingStatus : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enterText = findViewById(R.id.editText)
        imageButton = findViewById(R.id.imageButton)
        dowmloadingStatus = findViewById(R.id.textView)

        imageview = findViewById(R.id.imageView)

        messageHandler = Handler()
        downLoadService = DownLoadService()

        imageButton?.setOnClickListener {

            val urlString =  enterText?.text?.toString()

            if(urlString.equals("")){
                Toast.makeText(this,"!! URL is Blank !!",Toast.LENGTH_LONG).show()
            }else{
               if(checkInternetConnection() == true){

                   val newIntent = Intent(this,DownLoadService :: class.java)
                   newIntent.apply {
                       action = DownLoadService.ACTION_DOWNLOAD
                       putExtra(DownLoadService.EXTRA_URL,urlString)
                   }
                   dowmloadingStatus?.text = "DownLoading ..."
                   startService(newIntent)
               }
            }
        }
        Log.e("directory"," ${getExternalFilesDir("DownLoads")}/")
        val allFileName = ArrayList<String>()
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

    override fun messageShow(message: String?) {

        if(message == "Download completed"){
            readImage()
        }
        dowmloadingStatus?.setText(message)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(MyReceiver(this), IntentFilter(DownLoadService.ACTION_DOWNLOAD))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(MyReceiver(this))
    }

    override fun showingImage(message: Message) {
        //imageview?.setImageBitmap(message.data.getParcelable<Bitmap>("bitmap"))
    }
    private fun checkPermission() : Boolean{

        var checkPermissionResult = false
        val result = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if(result <= PackageManager.PERMISSION_GRANTED){
            checkPermissionResult = true
        }
        return checkPermissionResult
    }
//    private fun readImage(){
//
//        val fileName =  File("${getExternalFilesDir("DownLoads")}"+"")
//        val fileList : Array<File>? = fileName.listFiles()
//
//        if(checkPermission()){
//
//            Log.e("imageFile"," ${fileList?.get(0)?.name}")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                val source = ImageDecoder.createSource(assets,"RITAM_20200303_114016_instagram_PNG12.png")
//                val bitmap = ImageDecoder.decodeBitmap(source)
//                imageview?.setImageBitmap(bitmap)
//            }
//        }
//    }

    fun readImage(){

        try{
            val path = File("${getExternalFilesDir("DownLoads")}"+"")
            val fileList : Array<File>? = path.listFiles()
            Log.e("ImageFile"," ${fileList?.get(0)?.name}")

            val file =  File(path,fileList?.get(0)?.name)
            Log.e("Image"," ${file}")

            val bitmapImage = BitmapFactory.decodeStream(FileInputStream(file))
            var dstBmp : Bitmap? = null

            if (bitmapImage.width >= bitmapImage.height){
                dstBmp = Bitmap.createBitmap(bitmapImage, bitmapImage.getWidth()/2 - bitmapImage.getHeight()/2, 0, bitmapImage.getHeight(), bitmapImage.getHeight())
            }else{
                dstBmp = Bitmap.createBitmap(bitmapImage, 0, bitmapImage.getHeight()/2 - bitmapImage.getWidth()/2,bitmapImage.getWidth(),bitmapImage.getWidth());
            }

            imageview?.setImageBitmap(dstBmp)

        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}

interface ShowImage {
    fun showingImage(message : Message)
}

interface ShowStatus {
    fun messageShow(message: String?)
}
