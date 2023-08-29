package com.xinxiangshicheng.conversationapp.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.xinxiangshicheng.conversationapp.R

class ZeroItemWithIconView(context: Context, attributeSet: AttributeSet?) : LinearLayout(context, attributeSet) {
    private var titleTextView: TextView
    private var subtitleTextView: TextView

    private var imageView: ImageView

    private var titleText: String?
    private var subtitleText: String?


    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ZeroItemWithIconView, 0, 0)
        titleText = typedArray.getString(R.styleable.ZeroItemWithIconView_title)
        subtitleText = typedArray.getString(R.styleable.ZeroItemWithIconView_subTitle)
        val src = typedArray.getResourceId(R.styleable.ZeroItemWithIconView_android_src,R.drawable.ic_app)
        typedArray.recycle()
        this.gravity = 16
        inflate(context, R.layout.item_author, this)
        titleTextView = findViewById(R.id.title)
        subtitleTextView = findViewById(R.id.sub_title)
        imageView = findViewById(R.id.image)
        imageView.setImageResource(src)
        titleTextView.text = titleText
        subtitleTextView.text = subtitleText
    }

    fun setTitle(str: String?) {
        titleTextView.text = str
    }
}