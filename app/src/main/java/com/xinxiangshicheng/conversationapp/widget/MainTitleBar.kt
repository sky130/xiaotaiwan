package com.xinxiangshicheng.conversationapp.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.xinxiangshicheng.conversationapp.R

class MainTitleBar(context: Context, attributeSet: AttributeSet?) : LinearLayout(context, attributeSet) {
    private var titleTextView: TextView
    private var titleText: String?

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MainTitleBar, 0, 0)
        titleText = typedArray.getString(R.styleable.MainTitleBar_title)
        typedArray.recycle()
        this.gravity = 16
        inflate(context, R.layout.main_title_bar, this)
        titleTextView = findViewById(R.id.textView)
        titleTextView.text = titleText
        titleTextView.isSelected = true
        titleTextView.marqueeRepeatLimit = -1
    }

    fun setTitle(str: String?) {
        titleTextView.text = str
    }
}