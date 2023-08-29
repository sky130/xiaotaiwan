package com.xinxiangshicheng.conversationapp.logic.dao

import android.content.Context
import com.xinxiangshicheng.conversationapp.MainApplication
import com.xinxiangshicheng.conversationapp.util.SettingUtils

object AppDAO {

    private val sharedPreferences =
        MainApplication.context.getSharedPreferences("app", Context.MODE_PRIVATE)!!
    private val edit = SettingUtils(sharedPreferences)

    var index: Int
        get() = edit.get("index", 0)
        set(value) = edit.put("index", value)

}