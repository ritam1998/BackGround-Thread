package com.qi.asynctask

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var progressBar : ProgressBar? = null
    private var showButton : ImageButton? = null
    private var textMessage : EditText? = null

    private var ImageUrl : URL? = null
    private var inStream : InputStream? = null
    private var bitmap : Bitmap? = null

    private lateinit var asyncExample: AsyncExample

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showButton = findViewById(R.id.imageButton)
        textMessage = findViewById(R.id.editText)
        progressBar = findViewById(R.id.progressBar)

        showButton?.setOnClickListener {

            asyncExample = AsyncExample()
            asyncExample.execute(textMessage?.text?.toString())

        }
    }

     inner class AsyncExample : AsyncTask<String , String, Bitmap>(){

        override fun onPreExecute(){
            super.onPreExecute()

            progressBar?.visibility = View.VISIBLE
        }
        override fun doInBackground(vararg params: String?): Bitmap? {

            try {

                ImageUrl = URL(params.get(0))
                val conn: HttpURLConnection = ImageUrl?.openConnection() as HttpURLConnection
                conn.setDoInput(true)

                conn.connect()

                inStream = conn.getInputStream()
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.RGB_565

                bitmap = BitmapFactory.decodeStream(inStream, null, options)

            }catch (e : IOException){

            }
            return bitmap
        }
        override fun onPostExecute(bitmap: Bitmap){
            super.onPostExecute(bitmap)
            if(imageView != null){
                imageView.setImageBitmap(bitmap)
                progressBar?.visibility = View.GONE
            }
            else{
                progressBar?.visibility = View.VISIBLE
            }
        }

    }
}
