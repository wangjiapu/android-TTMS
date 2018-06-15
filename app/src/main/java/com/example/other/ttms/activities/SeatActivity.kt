package com.example.other.ttms.activities

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.other.ttms.R
import com.example.other.ttms.http.OkHttpUtils
import com.example.other.ttms.utils.JsonUtil
import com.example.other.ttms.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.activity_seat.*
import com.example.other.ttms.views.SeatTable
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import android.content.DialogInterface
import android.os.Build
import android.support.annotation.RequiresApi
import com.example.other.ttms.beans.OrderInfo
import com.example.other.ttms.beans.TicletInfo
import com.example.other.ttms.data.StaticData


class SeatActivity : Activity() {

    private var mPlanID = -1

    private var mStudioName = ""
    private var order_play_name = ""
    private var ticket_id = 0
    private var cinema = ""
    private var show_time = ""
    private var ticket_num = 1
    private var src = ""


    private var mTicketLists = ArrayList<TicletInfo>()
    private var row = 0
    private var col = 0


    private var map = HashMap<Int, Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat)


        val bundle = intent.extras
        mPlanID = bundle.getInt("PLANID")
        mStudioName = bundle.getString("STUDIONAME")
        cinema = bundle.getString("CINEMA")
        show_time = bundle.getString("SHOWTIME")
        src = bundle.getString("SRC")
        order_play_name = bundle.getString("ORDERPALYNAME")
        loadData()

        initView()

    }

    private fun initView() {

        confirm.setOnClickListener {

            confirm.visibility = View.INVISIBLE

            //检查登录状态
            val accountNumber = SharedPreferencesUtils.getString(SharedPreferencesUtils.FILENAME,
                    SharedPreferencesUtils.USENUMBER, "")


            showDialog()
            /* val checkLoginRequest = OkHttpUtils.CheckLogin(accountNumber)
             val checkCall = OkHttpUtils.okHttpClient.newCall(checkLoginRequest)
             checkCall.enqueue(object : Callback {
                 override fun onFailure(call: Call?, e: IOException?) {
                     Log.e("检查登录状态失败", e?.message)
                 }

                 override fun onResponse(call: Call?, response: Response?) {

                     if (response!!.isSuccessful) {
                         val result = JsonUtil.parseData(response.body()?.string())
                         runOnUiThread {
                             if (result != null) {
                                 //还在线
                                 showDialog()
                             } else {
                                 Toast.makeText(this@SeatActivity,
                                         "你还未登录，请先登录!", Toast.LENGTH_SHORT).show()
                             }
                         }

                     }
                 }

             })*/
        }

    }

    private fun showDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
                .setMessage("是否确认下单？")
                .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, id ->


                    for (entry in map.entries) {
                        val o = OrderInfo()

                        run breaking@{
                            mTicketLists.forEach {
                                if (it.row == entry.key && it.col == entry.value) {
                                    o.ticket_id = it.id
                                    return@breaking
                                }
                            }
                        }
                        o.src = src
                        o.ticket_num = ticket_num
                        o.show_time = show_time
                        o.order_play_name = order_play_name
                        o.cinema = cinema
                        o.isFinished = false
                        StaticData.unfinished.add(o)

                    }


                    Toast.makeText(this, "下单成功,请前往订单中心支付！", Toast.LENGTH_SHORT).show()
                    finish()
                })
                .setNegativeButton("取消", DialogInterface.OnClickListener { dialog, id ->
                    confirm.visibility = View.VISIBLE

                })
                .create()
        builder.show()
    }


    private fun loadData() {

        if (mPlanID == -1) {
            Toast.makeText(this, "获取座位信息失败", Toast.LENGTH_SHORT).show()
            return
        }
        val request = OkHttpUtils.getTicketByPlanId(mPlanID)
        val call = OkHttpUtils.okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("seatactivity获取座位信息失败", e?.message)
            }

            override fun onResponse(call: Call?, response: Response?) {

                if (response!!.isSuccessful) {
                    val result = JsonUtil.parseData(response.body()?.string())
                    if (result != null) {

                        mTicketLists = JsonUtil.parseArray(result, TicletInfo::class.java)
                                as ArrayList<TicletInfo>
                        mTicketLists.forEach {
                            if (it.row > row) {

                                row = it.row
                            }
                            if (it.col > col) {

                                col = it.col
                            }

                        }

                        runOnUiThread {

                            refreshView()
                        }

                    }
                }
            }
        })


    }

    private fun refreshView() {
        seatView.setScreenName("${mStudioName}演出厅")
        seatView.setMaxSelected(3)

        seatView.setSeatChecker(object : SeatTable.SeatChecker {

            override fun isValidSeat(row: Int, column: Int): Boolean {

                return column != 2

            }

            override fun isSold(row: Int, column: Int): Boolean {

                mTicketLists.forEach {
                    return it.status != 1
                }

                return false
            }

            override fun checked(row: Int, column: Int) {
                map.put(row, column)
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun unCheck(row: Int, column: Int) {

                map.remove(row, column)
            }

            override fun checkedSeatTxt(row: Int, column: Int): Array<String>? {
                return null
            }

        })
        seatView.setData(row, col)

    }

}