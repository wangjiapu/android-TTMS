package com.example.other.ttms.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.other.ttms.R
import com.example.other.ttms.activities.SeatActivity
import com.example.other.ttms.beans.CinemaInfo
import com.example.other.ttms.beans.ShowTimeInfo
import com.example.other.ttms.data.StaticData
import kotlinx.android.synthetic.main.showtime_item.view.*


class ShowTimeAdapter(val mContext: Context, val mlists: List<ShowTimeInfo>) :
        RecyclerView.Adapter<ShowTimeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.showtime_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mlists.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                holder.oBind(position)
            }
        }
    }


    inner class MyViewHolder(var root: View) : RecyclerView.ViewHolder(root),View.OnClickListener {

        override fun onClick(v: View?) {
            val intent= Intent(mContext, SeatActivity::class.java)
            val bundle=Bundle()
            bundle.putCharSequence("STUDIONAME",mlists[position].showStudioId)
            bundle.putCharSequence("CINEMA",StaticData.p[0].title)
            bundle.putCharSequence("SHOWTIME",mlists[position].startTime)
            bundle.putCharSequence("ORDERPALYNAME",StaticData.p[0].name)
            bundle.putCharSequence("SRC",StaticData.p[0].src)
            bundle.putInt("PLANID",mlists[position].planID)
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }

        fun oBind(position: Int) {
            root.setOnClickListener(this)
            root.showtime.text=mlists[position].startTime
            root.endtime.text="${mlists[position].endTime}散场"

            root.showtype.text="国语3D"
            root.studioID.text="${mlists[position].showStudioId}演出厅"
            root.price.text= "￥${mlists[position].price}"
        }

    }
}