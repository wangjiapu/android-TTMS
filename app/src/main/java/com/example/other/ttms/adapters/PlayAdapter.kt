package com.example.other.ttms.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.other.ttms.R
import com.example.other.ttms.beans.PlayInfo
import kotlinx.android.synthetic.main.play_item.view.*

class PlayAdapter(val mContext: Context, val mlists: List<PlayInfo>) : RecyclerView.Adapter<PlayAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.play_item, parent, false))
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


    inner class MyViewHolder(var root: View) : RecyclerView.ViewHolder(root) {

        fun oBind(position: Int) {
            Glide.with(mContext)
                    .load(mlists[position].src)
                    .error(mContext.resources.getDrawable(R.mipmap.error))
                    .into(root.image)

        }

    }
}