package com.qi.jsonapplication

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class WorkWithJson(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object{
        val ACTION_SEND = " com.qi.intentserviceapplication.action.SEND.DATA"
    }
    override fun doWork(): Result {

        return try {
            val stringData = inputData.getString("JSONVALUE")
            Log.e("DATA"," ${stringData}")
            JsonDataDownLoad(stringData)
            Result.success()
        }catch (e : Exception){
            e.printStackTrace()
            Result.failure()
        }
    }
    private fun JsonDataDownLoad(urlString : String?){

        val result = StringBuilder()
        lateinit var connection : HttpURLConnection
        try {

            val url  = URL(urlString)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            val responseMessage = connection.responseMessage

            if(responseCode == HttpURLConnection.HTTP_OK){
                val responseString = readStream(connection.inputStream)
                Log.e("JSONDATA"," $responseString")
                JSONPARSE(responseString)
            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
                Log.v("CatalogClient", "Response message:"+ responseMessage);
            }
        }catch (e : Exception){
            e.printStackTrace()
        }finally {
            if(connection != null){
                connection.disconnect()
            }
        }
    }
    private fun readStream(inputStream : InputStream) : String{

        var reader: BufferedReader? = null
        val response = StringBuffer()
        try {
            reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = ""
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return response.toString()
    }
    private fun JSONPARSE(jsonString : String){

        try{
            val backIntent = Intent(WorkWithJson.ACTION_SEND)
            backIntent.putExtra("JSONARRAY",jsonString)
            applicationContext.sendBroadcast(backIntent)

        }catch (e : java.lang.Exception){
            e.printStackTrace()
        }
    }
}