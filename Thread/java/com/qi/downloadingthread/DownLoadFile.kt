package com.qi.downloadingthread

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DownLoadFile(val urlString: String?,val showImageMessage: ShowImageMessage,val handler: Handler) : Thread() {

    override fun run(){

        var inStream : InputStream? = null
        var bitmapImage : Bitmap? = null

        val msg: Message = Message.obtain()
        msg.what = 1

        try{
            if (urlString != null) {
                inStream = openHttpConnection(urlString)
                Log.e("InputStream"," $inStream")
                bitmapImage = BitmapFactory.decodeStream(inStream)
            }
            val b = Bundle()

            b.putParcelable("bitmap", bitmapImage)
            msg.setData(b)

            inStream?.close()
            Log.e("Thread","background Thread Started $bitmapImage")

        }catch (e : IOException){

        }
        handler.post {
            showImageMessage.handleMessage(msg)

        }
    }

    private fun openHttpConnection(urlStr : String) : InputStream? {

        var inputStream : InputStream? = null
        var resCode = -1

        try {
            val url = URL(urlStr)

            val urlConnection = url.openConnection()

            if ((urlConnection !is HttpURLConnection)) {
                throw IOException("URL is not an Http URL");
            }

            val httpConnection = urlConnection as HttpURLConnection

            httpConnection.allowUserInteraction = false
            httpConnection.instanceFollowRedirects = true
            httpConnection.requestMethod = "GET"
            httpConnection.connect()

            resCode = httpConnection.responseCode

            if(resCode == HttpURLConnection.HTTP_OK){
                inputStream = httpConnection.inputStream
            }

        }catch (e : IOException){
            Log.e("Exception"," $e")
        }
        return inputStream
    }
}

