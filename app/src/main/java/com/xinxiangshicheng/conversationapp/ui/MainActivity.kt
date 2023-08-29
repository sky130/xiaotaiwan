package com.xinxiangshicheng.conversationapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.recyclerview.widget.LinearLayoutManager
import com.heytap.wearable.support.widget.HeySwitch
import com.xinxiangshicheng.conversationapp.adapter.AppItemAdapter
import com.xinxiangshicheng.conversationapp.adapter.AppItemAdapter.AppItem
import com.xinxiangshicheng.conversationapp.base.BaseActivity
import com.xinxiangshicheng.conversationapp.databinding.ActivityMainBinding
import com.xinxiangshicheng.conversationapp.service.JumpService
import com.xinxiangshicheng.conversationapp.util.AccessibilityUtils.isAccessibilitySettingsOn
import com.xinxiangshicheng.conversationapp.util.TextUtils.toast
import com.xinxiangshicheng.conversationapp.MainApplication.Companion.application as MainApplication

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.recycler.adapter = AppItemAdapter(
            arrayListOf(
                AppItem("小电视播放器", "支持弹幕解析"),
                AppItem("抬腕视频", "IJK解码器加持")
            )
        )
        binding.recycler.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.switchService.switch.isChecked = MainApplication.isStartService()
        binding.switchService.switch.setOnClickListener { switch ->
            switch as HeySwitch
            val isOn = isAccessibilitySettingsOn(JumpService::class.java.canonicalName!!)
            if (isOn) {
                "请先为小抬腕关闭无障碍服务".toast()
                switch.isChecked = true
                startSetting()
            } else {
                "请先为小抬腕开启无障碍服务".toast()
                switch.isChecked = false
                startSetting()
            }
        }
        binding.about.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        binding.recycler.isFocusable = false
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val isOn = isAccessibilitySettingsOn(JumpService::class.java.canonicalName!!)
        binding.switchService.switch.isChecked = isOn
    }

    private fun startSetting() {
        try {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            this.startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_SETTINGS)
            this.startActivity(intent)
        }
    }

}