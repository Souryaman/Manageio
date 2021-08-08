package com.example.manageio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.manageio.R
import com.example.manageio.firebase.FirestoreClass
import com.example.manageio.models.Board
import kotlinx.android.synthetic.main.item_board.view.*

open class BoardItemsAdapter(private val context: Context,private var list: ArrayList<Board>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

//    var lists = mutableListOf<Board>()
//    init {
//        this.lists = list as MutableList<Board>
//    }
    private var onClickListener : OnClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_board,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.color.lightPrimaryColor)
                .into(holder.itemView.iv_itemBoardImage)

            holder.itemView.tv_name.text = model.name
            holder.itemView.tv_createdBy.text = "Created By: ${model.createdBy}"

            holder.itemView.setOnClickListener {
                if(onClickListener!=null){
                    onClickListener!!.onClick(position,model)
                }
            }
        }
    }

    interface OnClickListener{
        fun onClick(position: Int,model : Board)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

//    fun del(position: Int){
//        FirestoreClass().deleteBoard()
//        lists.removeAt(position)
//        notifyDataSetChanged()
//    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)
}