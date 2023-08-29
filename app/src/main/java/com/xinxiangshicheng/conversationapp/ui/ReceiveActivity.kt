package com.xinxiangshicheng.conversationapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.xinxiangshicheng.conversationapp.adapter.VideoItemAdapter
import com.xinxiangshicheng.conversationapp.base.BaseActivity
import com.xinxiangshicheng.conversationapp.databinding.ActivityReceiveBinding
import com.xinxiangshicheng.conversationapp.logic.dao.AppDAO
import com.xinxiangshicheng.conversationapp.util.BiliUtils.getCid
import com.xinxiangshicheng.conversationapp.util.BiliUtils.request
import com.xinxiangshicheng.conversationapp.util.TextUtils.toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

class ReceiveActivity : BaseActivity() {
    private lateinit var binding: ActivityReceiveBinding
    private var bid = ""
    private var avid = 0
    private var pages: JSONArray? = null
    private var danmakuurl = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiveBinding.inflate(layoutInflater)
        binding.title.setBackListener(null,this)
        setContentView(binding.root)
        val uri = intent.data ?: return
        init(uri)
    }


    private fun init(uri: Uri) {
        bid = uri.getQueryParameter("bvid").toString()
        avid = uri.getQueryParameter("aid")!!.substring(2).toInt()
        val cid = uri.getQueryParameter("cid")
        danmakuurl = "https://comment.bilibili.com/$cid.xml"
        if (bid != "xinxiangshicheng") {
            if (avid == 0) {
                request(0, 0, bid, cid) { _, _ ->
                }
            } else {
                request(1, avid, null, cid) { _, _ ->
                }
            }
        } else {
            val okHttpClient = OkHttpClient()
            val request: Request =
                Request.Builder().url("https://api.bilibili.com/x/web-interface/view?aid=$avid")
                    .build()
            val call = okHttpClient.newCall(request)
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("小抬腕", "无障碍服务请求视频简介失败")
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body!!.string()
                    try {
                        val `object` = JSONObject(body)
                        val data = JSONObject(`object`["data"].toString())
                        pages = data.getJSONArray("pages")
                        var detail: JSONObject?
                        var i = 0
                        val list = ArrayList<String>()
                        while (true) {
                            try {
                                detail = pages!!.getJSONObject(i)
                                val number = detail.getString("page")
                                val part = detail.getString("part")
                                list.add("$number:$part")
                                i++
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                if (i == 1) {
                                    getCid(pages!!, avid, 0) { url, title ->
                                        startVideo(url, title)
                                    }
                                }
                                break
                            }
                        }
                        runOnUiThread {
                            binding.recycler.adapter = VideoItemAdapter(list).apply {
                                setOnClickListener {
                                    try {
                                        getCid(pages!!, avid, 0) { url, title ->
                                            startVideo(url, title)
                                        }
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })


        }
    }

    fun startVideo(videoUrl: String, title: String) {
        when (AppDAO.index) {
            0 -> { // 小电视播放器
                val intent = Intent().apply {
                    setClassName(
                        "com.xinxiangshicheng.wearbiliplayer.cn",
                        "com.xinxiangshicheng.wearbiliplayer.cn.player.PlayerActivity"
                    )
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("danmaku", danmakuurl)
                    putExtra("url", videoUrl)
                    putExtra("title", title)
                }

                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    "播放失败,可能是没有安装播放器".toast()
                }
                exitProcess(0)
            }

            1 -> { //抬腕视频在线播放
                //1.创建hankcode文件夹
                val basedir = Environment.getExternalStorageDirectory()
                val basedirectory = basedir.toString()
                val creat = File("$basedirectory/HankMi/")
                creat.mkdir()
                val creat1 = File("$basedirectory/HankMi/cache/")
                creat1.mkdir()
                val creat2 = File("$basedirectory/HankMi/cache/media/")
                creat2.mkdir()

                //2.创建握手文件
                val hankcode = "hmmedia=$videoUrl"
                val inputStream: InputStream =
                    ByteArrayInputStream(hankcode.toByteArray(StandardCharsets.UTF_8))
                val file = File("$basedirectory/HankMi/cache/media/", "keydata.hmd")
                try {
                    val outputStream = FileOutputStream(file)
                    var len = 0
                    val bytes = ByteArray(8)
                    while (inputStream.read(bytes).also { len = it } != -1) {
                        outputStream.write(bytes, 0, len)
                    }
                    inputStream.close()
                    outputStream.close()
                } catch (e: IOException) {
                    "创建文件夹失败,请给予应用所有权限".toast()
                }

                //3.打开抬腕视频
                try {
                    val packageManager = this.packageManager
                    val intent = packageManager.getLaunchIntentForPackage("com.hankmi.media")
                    intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } catch (e: Exception) {
                    "播放失败,播放器可能没有安装".toast()
                }
                System.exit(0)
            }
        }
    }

}