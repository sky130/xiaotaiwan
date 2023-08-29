package com.xinxiangshicheng.conversationapp.util

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

object BiliUtils {

    @Throws(JSONException::class)
    fun getCid(pages: JSONArray, avid: Int, id: Int, block: (String,String) -> Unit) {
        val detail = pages.getJSONObject(id)
        val cid = detail.getString("cid")
        request(1, avid, "xinxiangshicheng", cid, block)
    }

    fun requestTitle(id: Int, aid: Int, bvid: String?, videoUrl: String, block: (String,String) -> Unit) {
        val url = if (id == 0) {
            "https://api.bilibili.com/x/web-interface/view?bvid=$bvid"
        } else {
            "https://api.bilibili.com/x/web-interface/view?aid=$aid"
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    HttpClient.client.newCall(Request.Builder().url(url).build()).execute()
                val body = response.body?.string() ?: ""
                val data = JSONObject(JSONObject(body)["data"].toString())
                withContext(Dispatchers.Main) {
                    block(videoUrl,data.getString("title"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun processData(body: String?): String {
        return try {
            val data = JSONObject(JSONObject(body)["data"].toString())
            data.getString("title")
        } catch (e: Exception) {
            ""
        }
    }

    fun request(id: Int, aid: Int, bvid: String?, cid: String?, block: (String,String) -> Unit) {
        val url = if (id == 0) {
            "https://api.bilibili.com/x/player/playurl?bvid=$bvid&cid=$cid&qn=16&type=mp4&platform=html5"
        } else {
            "https://api.bilibili.com/x/player/playurl?avid=$aid&cid=$cid&qn=16&type=mp4&platform=html5"
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    HttpClient.client.newCall(Request.Builder().url(url).build()).execute()
                val body = response.body?.string()

                withContext(Dispatchers.Main) {
                    processData(body, id, aid, bvid, block)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun processData(
        body: String?,
        id: Int,
        aid: Int,
        bvid: String?,
        block: (String,String) -> Unit,
    ) {
        try {
            val data = JSONObject(JSONObject(body)["data"].toString())
            val durl = data.getJSONArray("durl")
            val videoUrl = durl.getJSONObject(0).getString("url")
            if (id == 0) {
                Log.e("1", "1")
                requestTitle(0, 0, bvid, videoUrl, block)
            } else {
                Log.e("else", "else")
                requestTitle(1, aid, null, videoUrl, block)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

object HttpClient {
    val client: OkHttpClient by lazy {
        OkHttpClient()
    }
}
