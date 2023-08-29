package com.xinxiangshicheng.conversationapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xinxiangshicheng.conversationapp.R
import com.xinxiangshicheng.conversationapp.logic.dao.AppDAO

class VideoItemAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<VideoItemAdapter.ViewHolder>() {

    data class VideoInfo(val number: String,val part:String)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.apply {
            title.text = list[position]
            itemView.setOnClickListener {
                block(position)
            }
        }
    }

    private var block: (Int) -> Unit = {}

    fun setOnClickListener(block: (Int) -> Unit) {
        this.block = block
    }

    fun getVideoList() = list

}