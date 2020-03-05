package com.qi.intentserviceapplication

import android.app.IntentService
import android.content.Intent
import android.os.Environment
import android.os.Message
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class DownLoadService : IntentService(""){


    companion object{
        val ACTION_DOWNLOAD = " com.qi.intentserviceapplication.action.DOWNLOAD"
        val ACTION_BAZ = "com.qi.intentserviceapplication.action.BAZ"

        val EXTRA_MESSAGE = "com.qi.intentserviceapplication.extra.message"
        val EXTRA_URL = "com.qi.intentserviceapplication.extra.URL"
    }

    val msg: Message = Message.obtain()

    override fun onHandleIntent(intent: Intent?) {

        val actionHandle = intent?.action

        if(ACTION_DOWNLOAD.equals(actionHandle)){
            val urlString = intent.getStringExtra(EXTRA_URL)
            downloadImage(urlString)
        }
    }
//    private fun downLoadImage(urlString : String){
//
//        Log.e("URL"," $urlString")
//
//        var resCode = -1
//        var inputStream : InputStream? = null
//        var bitmapImage : Bitmap? = null
//
//        var message = "DownLoad Failed .!"
//        val b = Bundle()
//
//        msg.what = 1

//        try{
//            val url = URL(urlString)
//
//            val urlConnection = url.openConnection()
//
//            if ((urlConnection !is HttpURLConnection)) {
//                throw IOException("URL is not an Http URL");
//            }
//
//            val httpConnection = urlConnection as HttpURLConnection
//
//            httpConnection.allowUserInteraction = false
//            httpConnection.instanceFollowRedirects = true
//            httpConnection.requestMethod = "GET"
//            httpConnection.connect()
//
//            resCode = httpConnection.responseCode
//
//            if(resCode == HttpURLConnection.HTTP_OK){
//
//                inputStream = httpConnection.inputStream
//                bitmapImage = BitmapFactory.decodeStream(inputStream)
//
//                val getImage = getImageUriFromBitmap(this,bitmapImage)
//
//                b.putParcelable("bitmap", getImage)
//                msg.setData(b)
//            }
//
//
//            inputStream?.close()
//
//        }catch (e : IOException){
//            Log.e("Exception"," $e")
//        }

//            val success = File("/sdcard/dirname").mkdir()
//            if(!success){
//                Toast.makeText(this,"Directory is not created",Toast.LENGTH_LONG).show()
//            }
//            try {
//
//                val url = URL(urlString)
//                val connection = url.openConnection() as HttpURLConnection
//                connection.doInput = true
//                connection.connect()
//
//                inputStream = connection.inputStream
//                bitmapImage = BitmapFactory.decodeStream(inputStream)
//
//                val data1 = String.format(
//                    "/sdcard/dirname/%d.png",
//                    System.currentTimeMillis()
//                )
//
//                val stream = FileOutputStream(data1)
//
//                val outstream = ByteArrayOutputStream()
//                bitmapImage.compress(Bitmap.CompressFormat.PNG, 85, outstream)
//                val byteArray = outstream.toByteArray()
//
//                stream.write(byteArray)
//                stream.close()
//
//                Toast.makeText(applicationContext, "Downloading Completed", Toast.LENGTH_SHORT).show()
//
//                message = "Download completed"
//
//            }catch (e : Exception){
//
//            }
//
//        val backIntent = Intent(DownLoadService.ACTION_DOWNLOAD)
//        backIntent.apply {
//            putExtra(DownLoadService.EXTRA_MESSAGE,message)
//        }
//        sendBroadcast(backIntent)
//    }

    private fun downloadImage(urlStr: String) {

        var fos: FileOutputStream? = null
        var inStream: InputStream? = null
        var message = "Download failed."

        try {
            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection

                connection.setDoInput(true)
                connection.connect()

            inStream = connection.inputStream

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
            val imageFileName = "RITAM_" + timeStamp + "_"

            val fileName = urlStr.substring(urlStr.lastIndexOf('/') + 1)

            fos = FileOutputStream(getExternalFilesDir("DownLoads").toString() +"/" + imageFileName +fileName)
            val buffer = ByteArray(1024)
            var count: Int

            while (inStream.read(buffer).also { count = it } > 0) {
                fos.write(buffer, 0, count)
            }

            fos.flush()
            message = "Download completed"

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                }
            }
            if (inStream != null) {
                try {
                    inStream.close()
                } catch (e: IOException) {
                }
            }
            val backIntent = Intent(DownLoadService.ACTION_DOWNLOAD)
            backIntent.putExtra(DownLoadService.EXTRA_MESSAGE, message)
            sendBroadcast(backIntent)
        }
    }
}
