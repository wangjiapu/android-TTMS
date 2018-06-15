package com.example.other.ttms.framgments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.other.ttms.R
import com.example.other.ttms.adapters.OrderAdapter
import com.example.other.ttms.beans.OrderInfo
import com.example.other.ttms.data.StaticData
import kotlinx.android.synthetic.main.fragment_seat.view.*

class OrderFragment : Fragment(), View.OnClickListener, OrderAdapter.OnItemClickListener {

    private lateinit var root: View

    private lateinit var mAdapter: OrderAdapter
    private lateinit var mAdapter2: OrderAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_seat, null)

        loadData()
        initView()
        return root
    }

    private fun loadData() {
       /* val order = OrderInfo()
        order.cinema = "长安区奥斯卡影城"
        order.order_play_name = "复仇者联盟"
        order.show_time = "12:30"
        order.ticket_num = 100
        for (i in 0..2) {
            StaticData.unfinished.add(order)
        }*/
    }

    private fun initView() {
        root.order.layoutManager = LinearLayoutManager(activity!!)
        mAdapter = OrderAdapter(activity!!, StaticData.unfinished)
        mAdapter2= OrderAdapter(activity!!, StaticData.finished)
        root.order.adapter = mAdapter
        mAdapter.setItemClickListener(this)
        root.unfinished.setOnClickListener(this)
        root.finished.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v) {
            root.unfinished -> {
                StaticData.unfinished.forEach {
                    it.isFinished=false
                }
               root.order.adapter=mAdapter
                root.finished.setTextColor(this.resources.getColor(R.color.normal))
                root.unfinished.setTextColor(this.resources.getColor(R.color.title))
            }
            root.finished -> {
                StaticData.finished.forEach {
                    it.isFinished=true
                }
               root.order.adapter=mAdapter2
                root.finished.setTextColor(this.resources.getColor(R.color.title))
                root.unfinished.setTextColor(this.resources.getColor(R.color.normal))
            }
        }
    }

    override fun onItemClick(position: Int) {
        StaticData.finished.add(StaticData.unfinished[position])
        StaticData.unfinished.removeAt(position)
        mAdapter.notifyItemRemoved(position)
    }
}