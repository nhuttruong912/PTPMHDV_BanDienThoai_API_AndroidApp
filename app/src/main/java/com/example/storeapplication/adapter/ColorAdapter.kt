package com.example.storeapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapplication.R
import com.example.storeapplication.databinding.ItemStorageBinding
import com.example.storeapplication.extension.setBackGroundDrawable
import com.example.storeapplication.model.Color
import com.example.storeapplication.ultil.CallBack
import java.util.*

class ColorAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listStorage: MutableList<Color> = arrayListOf()
                //listColor
    var oldPos = -1
    var currentPos: Int = 0

    private var callBackColor: CallBack.CallBackColor? = null

    fun callBackColor(callBackColor: CallBack.CallBackColor) {
        this.callBackColor = callBackColor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStorageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(mData: MutableList<Color>, pos: Int) {
        this.listStorage = mData
        this.currentPos = pos
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
        return listStorage.size
    }

    inner class ViewHolder(private val binding: ItemStorageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {
            binding.apply {
                tvBrand.text = listStorage[position].name.toString()
                //tvColor
                if (currentPos == position) {
                    binding.tvBrand.setBackGroundDrawable(R.drawable.background_brand)
                } else {
                    binding.tvBrand.setBackGroundDrawable(R.drawable.background_brand_noselect)
                }

                root.setOnClickListener {
                    callBackColor?.callBackColor(listStorage[position], position)
                }
            }
        }
    }
}