package com.xinxiangshicheng.conversationapp.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.xinxiangshicheng.conversationapp.MainApplication.Companion.context
import com.xinxiangshicheng.conversationapp.R
import com.xinxiangshicheng.conversationapp.ui.MainActivity
import com.xinxiangshicheng.conversationapp.util.TextUtils.log

class JumpService : AccessibilityService() {
    private val notificationManager =
        ContextCompat.getSystemService(context, NotificationManager::class.java)
    private var av: String = ""
    private var isCanPlay = false


    companion object {
        const val WRIST_BILI_PACKAGE_NAME = "cn.luern0313.wristbilibili"
        const val WRIST_BILI_VIDEO_ACTIVITY =
            "cn.luern0313.wristbilibili.ui.VideoActivity" // 视频界面类名
        const val WRIST_BILI_PLAYER_ACTIVITY =
            "cn.luern0313.wristbilibili.ui.PlayerActivity" // 播放器界面类名
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = AccessibilityServiceInfo().apply {
            eventTypes =
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_VIEW_CLICKED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
            packageNames = arrayOf(WRIST_BILI_PACKAGE_NAME)
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
            notificationTimeout = 100
        }
        "start2".log()
    }

    override fun onCreate() {
        val notificationId = 1
        val channelId = "1" // 通知渠道
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
        val builder = NotificationCompat.Builder(this, channelId).apply { // 通知构建
            setContentIntent(pendingIntent)
            setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            setSmallIcon(R.mipmap.ic_launcher)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "后台运行服务", NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager?.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }
        val notification = builder.build()
        notificationManager?.notify(notificationId, notification)
        startForeground(notificationId, notification)
        "start".log()

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val nodeInfo = event.source ?: return //当前界面的可访问节点信息
        if (nodeInfo.packageName.toString() != WRIST_BILI_PACKAGE_NAME) return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        when (event.className.toString()) {
            WRIST_BILI_VIDEO_ACTIVITY -> { //视频界面
                val list =
                    nodeInfo.findAccessibilityNodeInfosByViewId("cn.luern0313.wristbilibili:id/vd_video_aid")
                try {
                    av = list[0].text.toString()
                    isCanPlay = true
                } catch (_: Exception) {
                }
            }

            WRIST_BILI_PLAYER_ACTIVITY -> { // 播放器界面
                if (!isCanPlay) return
                nodeInfo.apply {
                    val uri =
                        "xinxiangshicheng://apiconversation:8888/receive?bvid=xinxiangshicheng&aid=$av&cid=0"
                    startActivity(Intent(Intent.ACTION_VIEW).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        data = Uri.parse(uri)
                    })
                    isCanPlay = false
                }
            }
        }
    }

    override fun onInterrupt() {}
}