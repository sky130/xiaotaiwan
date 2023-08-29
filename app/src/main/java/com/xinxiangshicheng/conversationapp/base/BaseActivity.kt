package com.xinxiangshicheng.conversationapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.remove(this)
    }

}