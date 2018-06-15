package com.example.other.ttms.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.other.ttms.R
import com.example.other.ttms.beans.OrderInfo
import kotlinx.android.synthetic.main.order_item.view.*

class OrderAdapter(val mContext: Context, val mlists: List<OrderInfo>) :
        RecyclerView.Adapter<OrderAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.order_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mlists.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.oBind(position)
    }


    inner class MyViewHolder(var root: View) : RecyclerView.ViewHolder(root), View.OnClickListener {
        override fun onClick(v: View?) {
            /**
             * 下单后逻辑
             */
           // Toast.makeText(mContext, "下单成功", Toast.LENGTH_SHORT).show()
            if (mItemClickListener!=null){
                mItemClickListener!!.onItemClick(position)
            }
        }

        fun oBind(position: Int) {

            root.order_play_name.text = mlists[position].order_play_name
            root.order_cinema_name.text = "影院： ${mlists[position].cinema}"

            root.order_show_time.text = "场次： ${mlists[position].show_time}"
            root.order_ticket_num.text = "数量： ${mlists[position].ticket_num}"


            Glide.with(mContext)
                    .load(mlists[position].src)
                    .error(R.mipmap.error)
                    .into(root.order_src)

            if (mlists[position].isFinished){
                root.order_buy.visibility=View.GONE
                root.order_buy2.visibility=View.VISIBLE
            }else{
                root.order_buy.visibility=View.VISIBLE
                root.order_buy2.visibility=View.GONE
            }
            root.order_buy.setOnClickListener(this)
        }

    }



    private var mItemClickListener: OnItemClickListener? = null

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        mItemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}