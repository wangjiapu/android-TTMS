package com.example.other.ttms.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.other.ttms.R
import com.example.other.ttms.activities.MainActivity
import com.example.other.ttms.activities.PlayActivity
import com.example.other.ttms.beans.CinemaInfo
import kotlinx.android.synthetic.main.cinema_item.view.*


class HomeAdapter(val mContext: Context, val mData: List<CinemaInfo>) :
        RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_NORMAL = 1
    val TYPE_G = 2

    private var mHeaderView: View? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        if (mHeaderView != null && viewType == TYPE_HEADER)
            return MyViewHolder(mHeaderView!!)
        if (viewType == TYPE_G){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.cinema_item_t, parent,
                    false)
            return MyViewHolder_t(view)
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cinema_item, parent,
                false)
        return MyViewHolder(view)
    }


    fun setHeaderView(headerView: View) {
        mHeaderView = headerView
    }

    override fun getItemCount(): Int {
        return if (mHeaderView == null) mData.size - 1 else mData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_HEADER) return

        val pos = getRealPosition(holder)

        if (getItemViewType(position) == TYPE_G){
            val h=holder as MyViewHolder_t
            h.onBind(pos)
        }else{
            holder.oBind(pos)
        }


    }


    private fun getRealPosition(holder: RecyclerView.ViewHolder): Int {
        val position = holder.layoutPosition
        return if (mHeaderView == null) position else position - 1
    }

    override fun getItemViewType(position: Int): Int {
        if (mHeaderView == null) {
            return TYPE_NORMAL
        }
        if (position == 0) {
            return TYPE_HEADER
        }
        if (position == 1) {
            return TYPE_G
        }
        return TYPE_NORMAL
    }


    open inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            val intent=Intent(mContext,PlayActivity::class.java)
            intent.putExtra("CINEMAID",mData[position].id)
            intent.putExtra("TITLE",mData[position].name)
            mContext.startActivity(intent)
        }

        fun oBind(position: Int) {
            if (itemView == mHeaderView) {
                return
            }
            itemView.cinemaName.text = mData[position].name
            itemView.cinemaAddress.text = mData[position].address

            itemView.setOnClickListener(this)

        }


    }

    inner class MyViewHolder_t(itemView: View):MyViewHolder(itemView), View.OnClickListener{
        override fun onClick(v: View?) {

        }
        fun onBind(position: Int){

        }

    }
}