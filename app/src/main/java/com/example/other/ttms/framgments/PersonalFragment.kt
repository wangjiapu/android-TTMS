package com.example.other.ttms.framgments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.other.ttms.ICallbacks.IPersonalCallback
import com.example.other.ttms.R
import com.example.other.ttms.activities.PersonalActivity
import com.example.other.ttms.beans.UserInfo
import com.example.other.ttms.http.OkHttpUtils
import com.example.other.ttms.utils.JsonUtil
import com.example.other.ttms.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.fragment_personal.view.*
import kotlinx.android.synthetic.main.layou_back.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class PersonalFragment : Fragment(), View.OnClickListener {


    public lateinit var mPersonalView: View
    lateinit var mCallback: IPersonalCallback             //Activity的回调方法
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallback = activity as PersonalActivity
    }

    val userInfo = UserInfo()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mPersonalView = inflater.inflate(R.layout.fragment_personal, null)
        initView()
        return mPersonalView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initonClickEvent()
    }

    private fun initonClickEvent() {
        mPersonalView.user_name_tv.setOnClickListener(this)
        mPersonalView.user_photo.setOnClickListener(this)
        mPersonalView.out_login.setOnClickListener(this)
        mPersonalView.back_tools.setOnClickListener(this)
        mPersonalView.ok.setOnClickListener(this)
    }

    /**
     * 初始化控件
     */
    private fun initView() {
        mPersonalView.out_login.setOnClickListener(this)
        mPersonalView.user_photo.setOnClickListener(this)
        mPersonalView.user_name_tv.setOnClickListener(this)
        userInfo.uerName = SharedPreferencesUtils.getString(SharedPreferencesUtils.FILENAME,
                SharedPreferencesUtils.USERNAME, "")
        userInfo.accountNumber = SharedPreferencesUtils.getString(SharedPreferencesUtils.FILENAME,
                SharedPreferencesUtils.USENUMBER, "")
        userInfo.src = SharedPreferencesUtils.getString(SharedPreferencesUtils.FILENAME,
                SharedPreferencesUtils.URL, "")

        if (!userInfo.src.equals("")) {
            Glide.with(context)
                    .load(userInfo.src)
                    .into(mPersonalView.photo)
        }

        mPersonalView.user_account_tv.text = "电话：${userInfo.accountNumber}"
        mPersonalView.user_name_tv.text = if (TextUtils.isEmpty(userInfo.uerName)) {
            "设置昵称"
        } else {
            userInfo.uerName
        }


    }


    override fun onClick(v: View?) {
        when (v) {
            mPersonalView.out_login -> {
                Toast.makeText(context, "退出登录", Toast.LENGTH_SHORT).show()
            }
            mPersonalView.user_photo -> {//上传头像

                mCallback.choicePhoto()
            }
            mPersonalView.user_name_tv -> { //修改昵称

                mPersonalView.et.visibility=View.VISIBLE
            }
            mPersonalView.ok ->{

                Toast.makeText(activity,"ok",Toast.LENGTH_SHORT).show()


                /**
                 * 上传图片
                 *
                 *
                 */

                val request=OkHttpUtils.uploadProfile()
                if (request==null){
                    Log.e("上传图片","URI失败")
                }else{
                    val call=OkHttpUtils.okHttpClient.newCall(request)
                    call.enqueue(object : Callback{
                        override fun onFailure(call: Call?, e: IOException?) {
                            Log.e("上传失败",e?.message)
                        }

                        override fun onResponse(call: Call?, response: Response?) {
                            if (response!!.isSuccessful){
                                val result=JsonUtil.parseData(response.body()?.string())
                                Log.e("上传成功","src=$result")
                            }
                        }

                    })
                }

            }

            mPersonalView.back_tools -> {
                mCallback.finishActivity()
            }
        }
    }


}