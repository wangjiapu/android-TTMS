package com.example.other.ttms.framgments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.other.ttms.R
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.other.ttms.adapters.HomeAdapter
import com.example.other.ttms.beans.CinemaInfo
import com.example.other.ttms.beans.PlayInfo
import com.example.other.ttms.http.OkHttpUtils
import com.example.other.ttms.utils.JsonUtil
import com.example.other.ttms.utils.NetUtil
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.autoplay_item.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.ArrayList


class HomeFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener {

    val REFRESH_COMPLETE = 10

    private lateinit var rootView: View
    private lateinit var myAdapter: HomeAdapter

    private val mData = ArrayList<PlayInfo>()
    private var mHandler: HomeHanlder? = null
    private var mCinemaInfos: MutableList<CinemaInfo> = ArrayList()

    private val mContext = this

    private var mHander: View? = null

    inner class HomeHanlder(homeFragment: HomeFragment) : Handler() {
        private val mFragment: WeakReference<HomeFragment>

        init {
            mFragment = WeakReference(homeFragment)
        }

        override fun handleMessage(msg: Message?) {
            val fg = mFragment.get()
            if (fg != null) {
                when (msg!!.what) {
                    REFRESH_COMPLETE -> {
                       // mHander!!.banner.update(mData)
                        fg.rootView.swipeRefreshLayout.isRefreshing = false
                    }
                    0 -> {//失败的话展示默认图片
                        for (i in 0..4) {
                            mData.add(PlayInfo())
                        }

                        mHander?.banner!!.setImages(mData)
                                .setImageLoader(GlideImageLoader())
                                .start()
                    }

                    1 -> {
                        mHander?.banner!!.setImages(mData)
                                .setImageLoader(GlideImageLoader())
                                .start()
                    }
                    2 -> {
                        myAdapter.notifyDataSetChanged()
                    }

                }

            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_home, null)
        mHandler = HomeHanlder(mContext)

        val c=CinemaInfo()
        c.name="奥斯卡国际影城"
        c.address="西安邮电大学"
        mCinemaInfos.add(c)
        mCinemaInfos.add(c)

        if (NetUtil.isNetworkAvailable(activity!!.application)){
            loadData()
        }else{
            //没有网的情况
        }

        initView()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        rootView.swipeRefreshLayout.setOnRefreshListener(this)
    }


    private fun loadData() {
        val request = OkHttpUtils.getShowMovie()
        val call = OkHttpUtils.okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("playFragment_loadData失败", e?.message)
                val msg = Message()
                msg.what = 0
                mHandler?.sendMessage(msg)
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response!!.isSuccessful) {
                    val result = JsonUtil.parseData(response.body()?.string())
                    val msg = Message()
                    msg.what = 0
                    if (result != null) {
                        mData.clear()
                        val list = JsonUtil.parseArray(result, PlayInfo::class.java)
                        mData.addAll(list)
                        msg.what = 1
                    }
                    mHandler?.sendMessage(msg)
                }
            }

        })


        val city_ID = 2768
        val cinemaRequest = OkHttpUtils.getCinema(city_ID)
        val cinemaCall = OkHttpUtils.okHttpClient.newCall(cinemaRequest)
        cinemaCall.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("获取市级以下的电影院失败", e?.message + "---------")

            }

            override fun onResponse(call: Call?, response: Response?) {

                if (response!!.isSuccessful) {
                    val msg = Message()
                    msg.what = 0
                    val result = JsonUtil.parseData(response.body()?.string())
                    if (result != null) {
                      //  mCinemaInfos.clear()
                        val list = JsonUtil.parseArray(result, CinemaInfo::class.java)
                         mCinemaInfos.addAll(list)
                        msg.what = 2
                    }
                    mHandler?.sendMessage(msg)
                }
            }

        })
    }


    private fun initView() {
        //渲染header布局
        mHander = LayoutInflater.from(activity).inflate(R.layout.autoplay_item, null)
        //设置banner的高度为手机屏幕的四分之一
        mHander?.banner!!.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getScreenH(activity!!) / 5)

        rootView.recyclerView.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)

        myAdapter = HomeAdapter(activity!!, mCinemaInfos)
        myAdapter.setHeaderView(mHander!!.banner)
        rootView.recyclerView.adapter = myAdapter
    }

    override fun onRefresh() {
        mHandler!!.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000)

    }


    /**
     * 获取屏幕高度
     * @param aty
     * @return
     */
    fun getScreenH(aty: Context): Int {
        val dm = aty.resources.displayMetrics
        return dm.heightPixels
    }


    override fun onDestroy() {
        super.onDestroy()
        mHandler?.removeCallbacksAndMessages(null)
    }


    class GlideImageLoader : ImageLoader() {
        override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
            val playInfo = path as PlayInfo
            Glide.with(context)
                    .load(playInfo.src)
                    .error(R.mipmap.error)
                    .into(imageView)
        }


    }




}