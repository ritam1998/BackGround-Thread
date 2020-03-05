package com.qi.jsonapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AllDataView(val wordList : List<WordModel>) : RecyclerView.Adapter<AllDataView.DataViewHolder>() {

    override fun onBindViewHolder(holder: AllDataView.DataViewHolder, position: Int) {
        holder.bindViewHolder(wordList[position])
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllDataView.DataViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_recyclerview,parent,false)
        return DataViewHolder(view)
    }

    class DataViewHolder(val itView : View) : RecyclerView.ViewHolder(itView){

        val wordName = itView.findViewById(R.id.Viewname) as TextView
        val wordFrequency = itView.findViewById(R.id.ViewFrequency) as TextView

        fun bindViewHolder(wordModel: WordModel){

            wordName.text = wordModel.wordData
            wordFrequency.text = wordModel.frequency.toString()
        }
    }
}