package com.xinxiangshicheng.conversationapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xinxiangshicheng.conversationapp.R
import com.xinxiangshicheng.conversationapp.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        binding.backTitleBar.setBackListener(null, this)
        setContentView(binding.root)
    }
}