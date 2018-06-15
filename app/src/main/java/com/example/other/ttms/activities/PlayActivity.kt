package com.example.other.ttms.activities

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.other.ttms.ICallbacks.OnPageSelectListener
import com.example.other.ttms.R
import com.example.other.ttms.adapters.ShowTimeAdapter
import com.example.other.ttms.beans.PlayInfo
import com.example.other.ttms.beans.ShowTimeInfo
import com.example.other.ttms.data.P
import com.example.other.ttms.data.StaticData
import com.example.other.ttms.http.OkHttpUtils
import com.example.other.ttms.utils.JsonUtil
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.layout_back.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.jetbrains.anko.custom.onUiThread
import java.io.IOException
import java.util.ArrayList

class PlayActivity : Activity() {

    private var mPlayLists = ArrayList<PlayInfo>()
    private val mShowTimes = ArrayList<ShowTimeInfo>()

    private var cinemaID = -1


    private var title=""
    private val images = ArrayList<View>()
    private var id=0

    private var showTimeAdapter: ShowTimeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        setContentView(R.layout.activity_play)
        cinemaID = intent.getIntExtra("CINEMAID", -1)
        title=intent.getStringExtra("TITLE")
        loadData()
        Handler().postDelayed(Runnable {
            initView()
        },1000)
       //  Thread.sleep(1000)
    }

    private fun initView() {

        show_time.layoutManager = LinearLayoutManager(this)
        showTimeAdapter = ShowTimeAdapter(this, mShowTimes)
        show_time.adapter = showTimeAdapter
        back.setOnClickListener {
            finish()
        }
        text.text=title

    }

    private fun loadData() {
        Log.e("cinemaID", "${cinemaID}")

        if (cinemaID == -1) {
            return
        }

        val request = OkHttpUtils.getMovieByCinemaID(cinemaID)
        val playCall = OkHttpUtils.okHttpClient.newCall(request)

        playCall.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("获取电影院剧目信息失败,", e?.message)
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response!!.isSuccessful) {
                    val result = JsonUtil.parseData(response.body()?.string())
                    if (result != null) {
                        Log.e("电影信息", result.toString() + "----------")
                        mPlayLists = JsonUtil.parseArray(result, PlayInfo::class.java)
                                as ArrayList<PlayInfo>
                        onUiThread {
                            setPlayImage()
                            initCover()

                        }
                    }
                }

            }

        })

    }

    private fun initCover() {
        cover.setViewList(images)
        cover.setOnPageSelectListener(object : OnPageSelectListener {
            override fun select(position: Int) {
                play_name.text = mPlayLists[position].name
                other_info.text = "时长：${mPlayLists[position].timeLength} |" +
                        "产地：${mPlayLists[position].makePlace} | 类别：" +
                        "${mPlayLists[position].kinds}"
                getShowTime(mPlayLists[position].id)
                pb.visibility = View.VISIBLE
            }

        })

        for (i in 0..images.size) {
            // list[i].setOnClickListener { Toast.makeText(activity, "点击了${i}", Toast.LENGTH_SHORT).show() }
        }

    }

    /**
     * 根据电影id获取演出计划
     */
    private fun getShowTime(id: Int) {

        this.id=id
        val request = OkHttpUtils.getPlanByMovieId(id)
        val playCall = OkHttpUtils.okHttpClient.newCall(request)

        playCall.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("获取演出计划失败,", e?.message)
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response!!.isSuccessful) {
                    val result = JsonUtil.parseData(response.body()?.string())
                    if (result != null) {
                        Log.e("演出计划信息", result.toString() + "----------")
                        val list = JsonUtil.parseArray(result, ShowTimeInfo::class.java)
                        mShowTimes.clear()
                        mShowTimes.addAll(list)
                        onUiThread {
                            pb.visibility = View.GONE
                            val p= P()
                            p.name=mPlayLists[id].name
                            p.src=mPlayLists[id].src
                            p.title=title
                            StaticData.p.clear()
                            StaticData.p.add(p)
                            showTimeAdapter?.notifyDataSetChanged()
                            //initView()
                        }
                    }
                }

            }

        })

    }


    /**
     * 设置影片的海报
     */
    private fun setPlayImage() {
        mPlayLists.forEach {
            val img = ImageView(this)
            img.scaleType = ImageView.ScaleType.FIT_XY
            Glide.with(this)
                    .load(it.src)
                    .into(img)
            images.add(img)
        }
    }


}