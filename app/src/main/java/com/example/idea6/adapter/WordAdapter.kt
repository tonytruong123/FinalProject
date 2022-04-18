package com.example.idea6.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.idea6.R
import com.example.idea6.customdict.CustomDict
import com.example.idea6.customdict.CustomDictViewModel


class WordAdapter(
    private val customDictViewModel: CustomDictViewModel
) : ListAdapter<CustomDict, WordAdapter.CustomDictViewHolder>(WordsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomDictViewHolder {
        return CustomDictViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CustomDictViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name, current.definition, customDictViewModel)
    }

    class CustomDictViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val word_mainView: TextView = itemView!!.findViewById(R.id.main_word)
        val word_defView: TextView = itemView!!.findViewById(R.id.word_def)
        val del_button: Button = itemView!!.findViewById(R.id.remove_word)

        fun bind(text: String?, definition: String?, customDictViewModel: CustomDictViewModel) {
            word_mainView.text = text
            word_defView.text = definition
            del_button.setOnClickListener{
                if (text != null) {
                    customDictViewModel.delete(text)
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): CustomDictViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.word_vertical, parent, false)
                return CustomDictViewHolder(view)
            }
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<CustomDict>() {
        override fun areItemsTheSame(oldItem: CustomDict, newItem: CustomDict): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CustomDict, newItem: CustomDict): Boolean {
            return oldItem.name == newItem.name
        }
    }
}



//class WordAdapter (
//
//    //Word Class Placeholder for testing
//    private val dataset: List<String>
//
//): RecyclerView.Adapter<WordAdapter.WordViewHolder>(){
//
//    class WordViewHolder(view: View?):RecyclerView.ViewHolder(view!!){
//        val word_mainView: TextView = view!!.findViewById(R.id.main_word)
//        val word_pronView: TextView = view!!.findViewById(R.id.word_pron)
//        val word_defView: TextView = view!!.findViewById(R.id.word_def)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
//        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.word_vertical, parent, false)
//        return WordViewHolder(adapterLayout)
//    }
//
//    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
//        holder.word_mainView.text = dataset[position]
//    }
//
//    override fun getItemCount() = dataset.size
//
//}