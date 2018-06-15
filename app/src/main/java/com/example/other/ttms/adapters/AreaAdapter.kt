package com.example.other.ttms.adapters

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.other.ttms.R
import com.example.other.ttms.beans.AreaInfo


class AreaAdapter(val mContext: Context, val mlist: List<AreaInfo>) : BaseAdapter() {



    override fun getItem(position: Int): Any {

        return mlist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mlist.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: Holder? = null
        var itemView = convertView
        if (convertView == null) {
            holder = Holder()
            itemView = View.inflate(mContext, R.layout.area_item, null)
            holder.areaName = itemView.findViewById(R.id.area_item_tv)
            itemView.tag = holder
        } else {
            holder = itemView?.tag as Holder?
        }
       // Log.e(mlist[position].area,mlist[position].id.toString())
        holder?.areaName?.text = mlist[position].area
        return itemView!!
    }


    inner class Holder {  //控件的类

        var areaName: TextView? = null


    }
}