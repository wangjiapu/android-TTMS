package com.example.other.ttms.http

import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.example.other.ttms.utils.SharedPreferencesUtils
import java.io.File
import okhttp3.*
import java.net.URI


object OkHttpUtils {


    private val BASEURL = "http://ttms.creatshare.com/public/index.php/front/"

    private val GETCODE = "Account/getCode"

    private val LOGIN = "Account/login"

    private val GETAREA = "Cinema/chooseArea"

    private val EXITLOGIN = "Account/exitLogin"

    private val CHECKLOGIN = "Account/checkLogin"

    private val GETSHOWMOVIE = "Movie/getShowMovie"

    private val GETMOVIEBYCINEMAID = "Movie/getMovieByCinemaId"

    private val GETCINEMA = "Cinema/getCinema"

    private val GETPLANBYMOVIEID = "Plan/getPlanByMovieId"

    private val UPLOADPROFILE = "Personal/uploadProfile"

    private val GETTICKETSBYPLANID="Ticket/getTicketsByPlanId"

    val okHttpClient = OkHttpClient()


    fun login(accountNumber: String, code: String): Request? {
        val fromBody: RequestBody = FormBody.Builder()
                .add("accountNumber", accountNumber)
                .add("code", code)
                .build()
        val builfer = Request.Builder()
        return builfer.url(BASEURL + LOGIN)
                .post(fromBody)
                .build()

    }

    /**
     * 获取验证码
     */

    fun getCode(num: String): Request {
        val fromBody: RequestBody = FormBody.Builder()
                .add("accountNumber", num)
                .build()
        val builfer = Request.Builder()
        return builfer.url(BASEURL + GETCODE)
                .post(fromBody)
                .build()

    }


    /**
     * 获取区级行政区的信息
     */

    fun getArea(cityId: Int): Request {
        val fromBody: RequestBody = FormBody.Builder()
                .add("city_id", cityId.toString())
                .build()
        val builder = Request.Builder()
        return builder.url(BASEURL + GETAREA)
                .post(fromBody)
                .build()
    }


    /**
     * 退出登录
     */
    fun exitLogin(num: String): Request {
        val fromBody: RequestBody = FormBody.Builder()
                .add("accountNumber", num)
                .build()
        val builder = Request.Builder()
        return builder.url(BASEURL + EXITLOGIN)
                .post(fromBody)
                .build()
    }


    /**
     * 检查登录
     */
    fun CheckLogin(num: String): Request {
        val fromBody: RequestBody = FormBody.Builder()
                .add("accountNumber", num)
                .build()
        val builder = Request.Builder()
        return builder.url(BASEURL + CHECKLOGIN)
                .post(fromBody)
                .build()
    }


    /**
     * 获取的主页要展示的电影
     *
     */
    fun getShowMovie(): Request {
        val builder = Request.Builder()
        return builder.get().url(BASEURL + GETSHOWMOVIE).build()

    }


    /**
     * 根据影院id获取上架的电影
     *
     */

    fun getMovieByCinemaID(cinema_id: Int): Request {
        val fromBody: RequestBody = FormBody.Builder()
                .add("cinema_id", cinema_id.toString())
                .build()
        val builder = Request.Builder()
        return builder.url(BASEURL + GETMOVIEBYCINEMAID)
                .post(fromBody)
                .build()
    }


    fun getCinema(area_id: Int): Request {
        val fromBody: RequestBody = FormBody.Builder()
                .add("area_id", area_id.toString())
                .build()
        val builder = Request.Builder()
        return builder.url(BASEURL + GETCINEMA)
                .post(fromBody)
                .build()
    }

    fun getPlanByMovieId(movie_id: Int): Request {
        val fromBody: RequestBody = FormBody.Builder()
                .add("movie_id", movie_id.toString())
                .build()
        val builder = Request.Builder()
        return builder.url(BASEURL + GETPLANBYMOVIEID)
                .post(fromBody)
                .build()
    }


    fun uploadProfile(): Request? {
        val num = SharedPreferencesUtils.getString(SharedPreferencesUtils.FILENAME,
                SharedPreferencesUtils.USERNAME, "")
        val uri = SharedPreferencesUtils.getString(SharedPreferencesUtils.FILENAME,
                SharedPreferencesUtils.URI, "")
        if (!TextUtils.isEmpty(uri)) {
            val file = File(URI(uri))
            if (!file.exists()) {
                Log.e(file.absolutePath + ":-------------", "未找到！")
                return null
            }
            val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("img", "${num}.jpg",
                            RequestBody.create(MediaType.parse("image/png"), file))

            val requestBody = builder.build()

            return Request.Builder()
                    .url(BASEURL + UPLOADPROFILE)
                    .post(requestBody)
                    .build()
        }
        return null
    }


    /**
     * 根据计划id来获取座位信息
     */
    fun getTicketByPlanId(plan_id: Int): Request {
        val fromBody: RequestBody = FormBody.Builder()
                .add("plan_id", plan_id.toString())
                .build()
        val builder = Request.Builder()
        return builder.url(BASEURL + GETTICKETSBYPLANID)
                .post(fromBody)
                .build()

    }

}