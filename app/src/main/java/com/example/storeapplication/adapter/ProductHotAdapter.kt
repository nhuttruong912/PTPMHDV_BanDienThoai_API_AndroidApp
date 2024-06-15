package com.example.storeapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storeapplication.databinding.ItemProductBinding
import com.example.storeapplication.extension.formatStringNumber
import com.example.storeapplication.model.PhoneDetailRoot
import com.example.storeapplication.ultil.CallBack
import java.util.*
import kotlin.collections.ArrayList

class ProductHotAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listProduct: MutableList<PhoneDetailRoot> = ArrayList()

    var oldPos = -1
    var currentPos: Int = 0

    private var callBackProduct: CallBack.CallBackProduct? = null

    fun callBackProduct(callBackProduct: CallBack.CallBackProduct) {
        this.callBackProduct = callBackProduct
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(mData: MutableList<PhoneDetailRoot>) {
        this.listProduct = mData
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
        return listProduct.size
    }

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {
            binding.apply {
                Glide.with(binding.root.context).load("https://cdn.tgdd.vn/Products/Images/42/281570/TimerThumb/iphone-15-(6).jpg").into(ivProduct)
                tvProduct.text = listProduct[position].phones[0].phoneName

                tvGB.text = "${listProduct[position].phones[0].builtInStorageCapacity} ${listProduct[position].phones[0].builtInStorageUnit}"

                tvCost.text = listProduct[position].phones[0].price.toString().formatStringNumber("#,###") + "đ"

                binding.tvMaMau.text = listProduct[position].phones[0].phoneColorName.toString() + " "

                binding.tvBrand.text = listProduct[position].brandName

                root.setOnClickListener {
                    callBackProduct?.callBackProduct(listProduct[position])
                }
            }
        }
    }
}