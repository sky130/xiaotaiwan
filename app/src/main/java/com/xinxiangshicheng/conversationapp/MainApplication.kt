package com.xinxiangshicheng.conversationapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import com.gyf.immersionbar.ImmersionBar
import com.xinxiangshicheng.conversationapp.service.JumpService

class MainApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var application: MainApplication
        lateinit var serviceIntent: Intent
    }

    private var isStart = false

    override fun onCreate() {
        super.onCreate()
        context = this
        application = this
        serviceIntent = Intent(this, JumpService::class.java)
    }

    fun startService() {
        if (isStart) return
        startService(serviceIntent)
        isStart = true
    }

    fun stopService() {
        if (!isStart) return
        stopService(serviceIntent)
        isStart = false
    }

    fun isStartService() = isStart

}