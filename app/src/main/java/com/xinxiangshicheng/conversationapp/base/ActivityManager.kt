package com.xinxiangshicheng.conversationapp.base

import android.app.Activity

object ActivityManager {

    private val list = ArrayList<Activity>()

    fun add(activity: Activity) = list.add(activity)

    fun remove(activity: Activity) = list.remove(activity)

    fun destroy() = list.forEach { it.finish() }
        .apply { android.os.Process.killProcess(android.os.Process.myPid()) }

}