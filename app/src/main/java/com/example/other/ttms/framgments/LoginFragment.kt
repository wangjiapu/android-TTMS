package com.example.other.ttms.framgments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.other.ttms.R
import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView
import android.widget.Toast
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import com.example.other.ttms.ICallbacks.IPersonalCallback
import com.example.other.ttms.activities.PersonalActivity
import com.example.other.ttms.beans.UserInfo
import com.example.other.ttms.beans.VerCodeInfo
import com.example.other.ttms.http.OkHttpUtils
import com.example.other.ttms.utils.JsonUtil
import com.example.other.ttms.utils.SharedPreferencesUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference


class LoginFragment : Fragment(), View.OnClickListener {


    lateinit var mLoginView: View
    private var videoView: VideoView? = null

    private var mHandler: LoginHandler? = null


    lateinit var mCallback: IPersonalCallback

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallback = activity as PersonalActivity
    }


    inner class LoginHandler(fragment: LoginFragment) : Handler() {

        private val mFragment: WeakReference<LoginFragment>

        init {
            mFragment = WeakReference(fragment)
        }

        override fun handleMessage(msg: Message?) {
            val fg = mFragment.get()
            if (fg != null) {
                when (msg!!.what) {
                    1 -> {
                        Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show()
                        ver_code.visibility = View.GONE
                    }
                    2 -> {//登录成功
                        val userInfo = msg.obj as UserInfo
                        SharedPreferencesUtils.putString(SharedPreferencesUtils.FILENAME,
                                SharedPreferencesUtils.USERNAME, userInfo.uerName)
                        SharedPreferencesUtils.putString(SharedPreferencesUtils.FILENAME,
                                SharedPreferencesUtils.USENUMBER, userInfo.accountNumber)
                        SharedPreferencesUtils.putString(
                                SharedPreferencesUtils.FILENAME, SharedPreferencesUtils.URL,
                                userInfo.src
                        )

                        //通知主界面刷新ui
                        val mIntent = Intent("com.example.other.ttms")
                        //mIntent.putExtra("result", "跟新ui")
                        mLoginView.progressbar.visibility = View.GONE
                        context!!.sendBroadcast(mIntent)
                        mCallback.showPersonalFg()

                    }
                }

            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mLoginView = inflater.inflate(R.layout.fragment_login, null)
        initView()
        mHandler = LoginHandler(this)
        return mLoginView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initEvent()
    }


    private fun initEvent() {
        val uriStr = "android.resource://" + context!!.packageName + "/raw/ttmsl"
        val uri = Uri.parse(uriStr)
        if (uri == null) {
            Toast.makeText(context, "视频文件路径错误", Toast.LENGTH_SHORT).show()

        } else {

            val mp = android.widget.MediaController(context)
            mp.visibility = View.INVISIBLE
            videoView?.setMediaController(mp)

            videoView?.isClickable = false

            videoView?.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                videoView?.setVideoURI(uri)
                videoView?.start()
            })

            videoView?.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                override fun onPrepared(mp: MediaPlayer?) {
                    mp?.setVolume(0f, 0f)
                }

            })

            videoView?.setVideoURI(uri)
            videoView?.start()
        }


        login_tv.setOnClickListener(this)
        forgetpwd.setOnClickListener(this)
        registered.setOnClickListener(this)
        ver_code.setOnClickListener(this)
    }

    private fun initView() {
        videoView = mLoginView.findViewById<View>(R.id.videoview) as VideoView
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ver_code -> {     //发送验证码
                val uerStr = username.text.toString().trim()
                if (isMobileNO(uerStr)) {
                    val request = OkHttpUtils.getCode(uerStr)
                    val call = OkHttpUtils.okHttpClient.newCall(request)
                    call.enqueue(object : Callback {
                        override fun onFailure(call: Call?, e: IOException?) {
                            Log.e("onFailure", "发送失败")
                        }

                        override fun onResponse(call: Call?, response: Response?) {
                            if (response!!.isSuccessful) {
                                val gson = Gson()
                                val codeInfo = gson.fromJson(response.body()!!.string(), VerCodeInfo::class.java)

                                if (codeInfo != null && codeInfo.result.equals("success") && codeInfo.msg == 1) {
                                    Log.e("onResponse", "发送成功")
                                    val msg = Message()
                                    msg.what = 1
                                    mHandler?.sendMessage(msg)
                                }

                            }
                        }

                    })
                }
            }
            R.id.login_tv -> {
                mLoginView.progressbar.visibility = View.VISIBLE
                val userStr = username.text.toString().trim()
                val pwdStr = password.text.toString().trim()
                if (TextUtils.isEmpty(userStr)) {
                    Toast.makeText(context, "电话不能为空", Toast.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(pwdStr)) {
                    Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show()
                } else {
                    val request = OkHttpUtils.login(userStr, pwdStr)
                    val call = OkHttpUtils.okHttpClient.newCall(request)
                    call.enqueue(object : Callback {
                        override fun onFailure(call: Call?, e: IOException?) {
                            Log.e("onFailure", e?.message)
                            Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call?, response: Response?) {
                            if (response!!.isSuccessful) {
                                val body = response.body()!!.string()
                                Log.e("onResponse", body)
                                val result = JsonUtil.parseData(body)
                                if (result != null) {
                                    val gson = Gson()
                                    val userInfo = gson.fromJson(result, UserInfo::class.java)
                                    if (userInfo != null) {
                                        val msg = Message()
                                        msg.what = 2
                                        msg.obj = userInfo
                                        mHandler?.sendMessage(msg)
                                    }
                                }
                            }
                        }

                    })

                }
            }
            R.id.forgetpwd ->
                /**
                 * 忘记密码时使用
                 */
                Toast.makeText(context, "请继续关注！", Toast.LENGTH_SHORT).show()
            R.id.registered -> {
                /**
                 * 注册新用户时使用；
                 */
                // Toast.makeText(context, "请不要着急！", Toast.LENGTH_SHORT).show()
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy", "关闭播放")
        mHandler?.removeCallbacksAndMessages(null)
        videoView?.stopPlayback()

    }


    private fun isMobileNO(mobiles: String): Boolean {

        val telRegex = "[1][34578]\\d{9}"
        return if (TextUtils.isEmpty(mobiles)) {
            false
        } else {
            mobiles.matches(telRegex.toRegex())
        }
    }


}
