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

class AppItemAdapter(private val list: ArrayList<AppItem>) :
    RecyclerView.Adapter<AppItemAdapter.ViewHolder>() {

    data class AppItem(val title: String, val subTitle: String)

    private var c = AppDAO.index


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val radio: RadioButton = view.findViewById(R.id.radio)
        val title: TextView = view.findViewById(R.id.title)
        val subTitle: TextView = view.findViewById(R.id.sub_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.apply {
            title.text = list[position].title
            subTitle.text = list[position].subTitle
            radio.isChecked = position == c
            itemView.setOnClickListener {
                c = position
                notifyDataSetChanged()
                AppDAO.index = position
            }
        }
    }

    fun getAppList() = list

}