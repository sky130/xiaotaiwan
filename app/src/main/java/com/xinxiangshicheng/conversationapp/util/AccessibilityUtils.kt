package com.xinxiangshicheng.conversationapp.util

import android.provider.Settings
import android.text.TextUtils
import com.xinxiangshicheng.conversationapp.MainApplication.Companion.application
import com.xinxiangshicheng.conversationapp.MainApplication.Companion.context

object AccessibilityUtils {

    /**
     * 检测辅助功能是否开启
     *
     * @param mContext
     * @return boolean
     */
    fun isAccessibilitySettingsOn(serviceName: String): Boolean {
        var accessibilityEnabled = 0
        val service = "${context.packageName}/$serviceName"
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (_: Settings.SettingNotFoundException) {
        }
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                application.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}