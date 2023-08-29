package com.xinxiangshicheng.conversationapp.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.heytap.wearable.support.widget.HeySwitch
import com.xinxiangshicheng.conversationapp.R
import com.xinxiangshicheng.conversationapp.databinding.ZeroSwitchViewBinding

@SuppressLint("Recycle")
class ZeroSwitchView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    var binding: ZeroSwitchViewBinding
    var text = ""

    init {
        binding = ZeroSwitchViewBinding.inflate(LayoutInflater.from(context), this, true)
        val attrArray = context.obtainStyledAttributes(attrs, R.styleable.ZeroSwitchView)
        text = attrArray.getString(R.styleable.ZeroSwitchView_text).toString()
        attrArray.recycle()
        binding.textView.text = text
    }

    val switch: HeySwitch
        get() = binding.switchWidgetCustom


}