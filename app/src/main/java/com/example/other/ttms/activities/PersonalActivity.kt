package com.example.other.ttms.activities


import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.example.other.ttms.ICallbacks.IPersonalCallback
import com.example.other.ttms.R
import com.example.other.ttms.framgments.LoginFragment
import com.example.other.ttms.framgments.PersonalFragment
import com.example.other.ttms.utils.ImgUtil
import android.content.pm.PackageManager
import android.widget.Toast
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import android.content.Intent
import android.util.Log
import com.example.other.ttms.utils.SharedPreferencesUtils
import java.io.File


class PersonalActivity : AppCompatActivity(), IPersonalCallback {


    var type = ""

    val mLoginFragment = LoginFragment()
    val mPersonalFragment = PersonalFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

        setContentView(R.layout.activity_persional)
        type = intent.getStringExtra("TYPE")
        when (type) {
            "0" -> {//未登录哦状态

                supportFragmentManager.beginTransaction()
                        .add(R.id.content1, mLoginFragment)
                        .commitNow()
            }
            "1" -> {//登录状态
                supportFragmentManager.beginTransaction()
                        .add(R.id.content1, mPersonalFragment)
                        .commitNow()
            }
        }
    }


    override fun showPersonalFg() {
        supportFragmentManager.beginTransaction()
                .remove(mLoginFragment)
                .add(R.id.content1, mPersonalFragment)
                .commitNow()
    }

    /**
     * 退出登录
     */

    override fun finishActivity() {
        finish()
    }

    override fun choicePhoto() {
        ImgUtil.choicePhoto(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            ImgUtil.REQUEST_CODE_ALBUM//相册存储权限
            -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImgUtil.openAlbum(this)
            } else {
                Toast.makeText(this, "选择图库需要同意权限", Toast.LENGTH_SHORT).show()
            }
            ImgUtil.REQUEST_CODE_CAMERA//相机拍照权限
            -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//允许
                ImgUtil.openCamera(this)
            } else {//拒绝
                Toast.makeText(this, "只有同意相机权限,才能使用扫码功能", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //相机、相册、剪切 返回
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //正确返回
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ImgUtil.TAKE_PHOTO//相机返回
                ->
                    //相机返回图片，调用裁剪的方法
                    ImgUtil.startUCrop(this, ImgUtil.imageUri, 1f, 1f)
                ImgUtil.CHOOSE_PHOTO//相册返回
                -> try {
                    if (data != null) {
                        val uri = data.data
                        //相册返回图片，调用裁剪的方法
                        ImgUtil.startUCrop(this, uri, 1f, 1f)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("--------------", e.message)
                    Toast.makeText(this, "图片选择失败1", Toast.LENGTH_SHORT).show()
                }

                UCrop.REQUEST_CROP//剪切返回
                -> {
                    val resultUri = UCrop.getOutput(data!!)
                    Log.e("0000000000", resultUri.toString())
                    SharedPreferencesUtils.putString(SharedPreferencesUtils.FILENAME,
                            SharedPreferencesUtils.URI, resultUri.toString())
                    //剪切返回，显示剪切的图片到布局
                    Glide.with(this)
                            .load(resultUri)
                            .placeholder(R.mipmap.ic_launcher)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(mPersonalFragment.mPersonalView.findViewById(R.id.photo))
                }
            }
        } else {
            Toast.makeText(this, "图片选择失败2", Toast.LENGTH_SHORT).show()
        }
    }

}