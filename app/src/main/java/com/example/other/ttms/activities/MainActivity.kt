package com.example.other.ttms.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.other.ttms.R
import com.example.other.ttms.framgments.HomeFragment
import com.example.other.ttms.beans.CityInfo
import com.example.other.ttms.beans.UserInfo
import com.example.other.ttms.framgments.OrderFragment
import com.example.other.ttms.http.OkHttpUtils
import com.example.other.ttms.utils.JsonUtil
import com.example.other.ttms.utils.LocationUtil
import com.example.other.ttms.utils.NetUtil
import com.example.other.ttms.utils.SharedPreferencesUtils
import com.example.other.ttms.views.PopWinArea
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //座位管理fragment
    val mOrderFragment: Fragment = OrderFragment()
    //演出厅管理fragment

    val mHomeFragment: HomeFragment = HomeFragment()

    var mPersional: ImageView? = null
    var mName: TextView? = null

    var TYPE = 0//是否是登录状态

    var mBroadcastReceiver: BroadcastReceiver? = null

    val userInfo = UserInfo()


    var locationInfo: CityInfo? = null
    var mListWindow: PopWinArea? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initFragment()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        initdata(nav_view.getHeaderView(0))

    }

    private fun initdata(headerView: View?) {
        /**
         * 判断是否需要登录
         * 获取信息
         * 刷新界面
         */

        mPersional = headerView?.findViewById(R.id.imageView)
        mName = headerView?.findViewById(R.id.name)
        getUserInfo()
        if (TextUtils.isEmpty(userInfo.accountNumber)) {
            //没有保存数据，不需要发送请求获取登录信息
            mName?.text = content.resources.getString(R.string.nologin)
            TYPE = 0

        } else {


            mName?.text = userInfo.uerName
            Glide.with(this)
                    .load(userInfo.src)
                    .error(R.mipmap.ic_launcher)
                    .into(mPersional)
            /**
             * 从本地文件中都去后台数据
             * 开启线程去发送告诉服务器我又来了
             *
             */

            if (NetUtil.isNetworkAvailable(this)) {
                val checkRequest = OkHttpUtils.CheckLogin(userInfo.accountNumber)
                val checkCall = OkHttpUtils.okHttpClient.newCall(checkRequest)
                checkCall.enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {

                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        if (response!!.isSuccessful) {
                            val result = JsonUtil.parseData(response.body()?.string())
                            if (result != null) {
                                TYPE = 1
                            } else {
                                TYPE = 0
                            }
                        }
                    }

                })
            }

        }
        mPersional?.setOnClickListener {
            val intent = Intent(this, PersonalActivity::class.java)
            intent.putExtra("TYPE", TYPE.toString())
            startActivity(intent)
        }
        getLocation()

    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                return true
            }

            R.id.action_location -> {
                //选择区
                if (LocationUtil.areas != null) {
                    if (mListWindow == null) {
                        mListWindow = PopWinArea(this, LocationUtil.areas)
                        mListWindow!!.contentView.onFocusChangeListener = (object : View.OnFocusChangeListener {
                            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                                if (!hasFocus) {
                                    mListWindow!!.dismiss()
                                }
                            }

                        })
                    }

                    mListWindow!!.isFocusable = true
                    mListWindow!!.showAsDropDown(findViewById(R.id.action_location), 0, 0)
                    mListWindow!!.update()
                }
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_camera -> {
                showFragment(mOrderFragment)

            }
            R.id.home -> {
                showFragment(mHomeFragment)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * 初始化fragment
     */
    private fun initFragment() {
        supportFragmentManager.beginTransaction()
                .add(R.id.content, mOrderFragment)
                .add(R.id.content, mHomeFragment)
                .commitNow()

        supportFragmentManager.beginTransaction()
                .hide(mOrderFragment)
                .commitNow()

    }

    /**
     * 显示我们需要选定的fragment
     */
    private fun showFragment(fg: Fragment) {
        hideFragment()
        supportFragmentManager.beginTransaction()
                .show(fg)
                .commitAllowingStateLoss()

    }

    /**
     * 隐藏不需要的fargment
     */
    private fun hideFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(mOrderFragment)
                .hide(mHomeFragment)
                .commitNowAllowingStateLoss()
    }


    override fun onResume() {
        super.onResume()

        /**
         * 注册广播
         */

        mBroadcastReceiver = MyBroadcastReceiver()
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction("com.example.other.ttms")
        registerReceiver(mBroadcastReceiver, mIntentFilter)


    }


    override fun onDestroy() {
        super.onDestroy()
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver)
        }
    }

    /**
     * 更换界面
     * 读取消息
     */
    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            getUserInfo()
            if (!TextUtils.isEmpty(userInfo.src)) {
                Glide.with(this@MainActivity)
                        .load(userInfo.src)
                        .into(mPersional)
            }
            mName?.text = if (TextUtils.isEmpty(userInfo.uerName)) {
                "未设置昵称，前往设置"
            } else {
                userInfo.uerName
            }
        }

    }


    /**
     * 从SharedPreferences中获取数据
     */

    private fun getUserInfo() {
        userInfo.uerName = SharedPreferencesUtils.getString(SharedPreferencesUtils.FILENAME,
                SharedPreferencesUtils.USERNAME, "")
        userInfo.accountNumber = SharedPreferencesUtils.getString(SharedPreferencesUtils.FILENAME,
                SharedPreferencesUtils.USENUMBER, "")
        userInfo.src = SharedPreferencesUtils.getString(SharedPreferencesUtils.FILENAME,
                SharedPreferencesUtils.URL, "")
        val uri = SharedPreferencesUtils.getString(SharedPreferencesUtils.FILENAME,
                SharedPreferencesUtils.URI, "")
        if (TextUtils.isEmpty(uri)) {
            userInfo.src = uri
        }
    }

    /**
     * 开启定位功能区获取本地区
     *
     */
    private fun getLocation() {
        //开启子线程获取，并且更新。
        /***
         * 定位之后城市，同时获取到所在城市下的区名
         *
         */
        locationInfo = LocationUtil.getCity()
        LocationUtil.getArea(locationInfo!!.id)

    }


}
