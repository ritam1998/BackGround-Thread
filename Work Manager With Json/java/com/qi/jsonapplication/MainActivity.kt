package com.qi.jsonapplication

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import org.json.JSONObject

class MainActivity : AppCompatActivity(),SHOWALLJSONDATA {

    private var downLoadButton : Button? = null
    private var downLoadStatus : TextView? = null

    private lateinit var wordModel: WordModel
    private var recyclerView : RecyclerView? = null

    private var wordList = ArrayList<WordModel>()
    private lateinit var allDataView: AllDataView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        downLoadButton = findViewById(R.id.downloadbutton)
        downLoadStatus = findViewById(R.id.jsonTextView)

        recyclerView = findViewById(R.id.recyclerview)

        downLoadButton?.setOnClickListener {
            val request = OneTimeWorkRequest.Builder(WorkWithJson :: class.java).setInputData(sendJsonData()).build()
            WorkManager.getInstance(this).enqueue(request)
        }
    }
    private fun sendJsonData() : Data{
        return Data.Builder().putString("JSONVALUE","http://a.galactio.com/interview/dictionary-v2.json").build()
    }
    override fun showAllData(jsonString: String?) {
        workingWithJsoData(JSONObject(jsonString))
    }

    private fun workingWithJsoData(jsonObject: JSONObject?){

        wordList.clear()
        Log.e("Parse","$jsonObject")
        val data = jsonObject?.getJSONArray("dictionary")
        Log.e("Length"," ${data?.getJSONObject(0)?.getString("word")}")

        for(i in 0..((data?.length() ?: 0) -1) ){

            val getJsonData = data?.getJSONObject(i)
            val word = getJsonData?.getString("word")

            val frequency = getJsonData?.getInt("frequency")
            Log.e("words are : "," $word")

            wordList.add(WordModel(wordData = word,frequency = frequency))
        }
        recyclerView?.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        allDataView = AllDataView(wordList)
        recyclerView?.adapter = allDataView
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(MyReceiver(this), IntentFilter(WorkWithJson.ACTION_SEND))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(MyReceiver(this))
    }
}

interface SHOWALLJSONDATA {
    fun showAllData(jsonString : String?)
}
