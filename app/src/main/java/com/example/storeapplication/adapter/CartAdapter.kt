package com.example.storeapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storeapplication.databinding.ItemCartBinding
import com.example.storeapplication.extension.formatStringNumber
import com.example.storeapplication.extension.hide
import com.example.storeapplication.model.Item
import com.example.storeapplication.ultil.CallBack
import java.util.*

class CartAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listItemRoot: MutableList<Item> = arrayListOf()

    var oldPos = -1
    var currentPos: Int = 0
    var checkBox: Boolean = false

    private var callBackSelectMany: CallBack.CallBackSelectMany? = null

    fun callBackSelectMany(callBackSelectMany: CallBack.CallBackSelectMany) {
        this.callBackSelectMany = callBackSelectMany
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(mData: MutableList<Item>) {
        this.listItemRoot = mData
    }

    fun checkBox(checkBox: Boolean) {
        this.checkBox = checkBox
        notifyDataSetChanged()
    }

    fun checkSelect(pos: Int) {
        oldPos = currentPos
        currentPos = pos
        notifyItemChanged(pos)
        notifyItemChanged(oldPos)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindData(position)
    }

    override fun getItemCount(): Int {
        return listItemRoot.size
    }
    //view holder cho item cart binding
    inner class ViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {
            if (!checkBox) binding.backgroundBuy.hide()
            binding.apply {
                Glide.with(binding.root.context).load("https://imgs.sundaymore.com/wp-content/uploads/2023/06/img_2036_3563603564919217acc2f.jpg").into(ivProduct)
                tvProduct.text = listItemRoot[position].phoneName
                tvCost.text = listItemRoot[position].price.toString().replace(".000", "").replace(".", "").formatStringNumber("#,###") + "đ"

                binding.root.setOnClickListener {
                    callBackSelectMany?.callBackSelectMany(listItemRoot[position], position)
                }
            }
        }
    }
}