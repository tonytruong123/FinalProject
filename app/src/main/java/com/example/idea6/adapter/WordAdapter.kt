package com.example.prototype1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.idea6.R


class WordAdapter (

    //Word Class Placeholder for testing
    private val dataset: List<String>

): RecyclerView.Adapter<WordAdapter.WordViewHolder>(){

    class WordViewHolder(view: View?):RecyclerView.ViewHolder(view!!){
        val word_mainView: TextView = view!!.findViewById(R.id.main_word)
        val word_pronView: TextView = view!!.findViewById(R.id.word_pron)
        val word_defView: TextView = view!!.findViewById(R.id.word_def)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.word_vertical, parent, false)
        return WordViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.word_mainView.text = dataset[position]
    }

    override fun getItemCount() = dataset.size

}