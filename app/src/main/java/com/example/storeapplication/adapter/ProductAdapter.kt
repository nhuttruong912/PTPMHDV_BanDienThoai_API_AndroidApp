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

class ProductAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listProduct: MutableList<PhoneDetailRoot> = ArrayList()

    var oldPos = -1
    var currentPos: Int = 0

    private var callBackProduct: CallBack.CallBackProduct? = null
    //ham xly khi sp dc chon
    fun callBackProduct(callBackProduct: CallBack.CallBackProduct) {
        this.callBackProduct = callBackProduct
    }
    //ktao va return viewholder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    //them sp vao adapter
    @SuppressLint("NotifyDataSetChanged")
    fun addAll(mData: MutableList<PhoneDetailRoot>) {
        this.listProduct = mData
    }
    //cap nhat vi tri sp da chon
    fun checkSelect(pos: Int) {
        oldPos = currentPos
        currentPos = pos
        notifyItemChanged(pos)
        notifyItemChanged(oldPos)
    }
    //goi binddata() de lket du lieu voi viewholder
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindData(position)
    }
    //so luong sp trong ds
    override fun getItemCount(): Int {
        return listProduct.size
    }
    //hien thi du lieu vao itemProductBinding
    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {
            binding.apply {
//                Glide.with(binding.root.context).load(listProduct[position].phones[0].phoneColorUrl).into(ivProduct)
                Glide.with(binding.root.context).load("https://imgs.sundaymore.com/wp-content/uploads/2023/06/img_2036_3563603564919217acc2f.jpg").into(ivProduct)
                tvProduct.text = listProduct[position].phones[0].phoneName

                val numberGb = if (listProduct[position].phones[0].builtInStorageCapacity.toString() == "-1") "256"
                else listProduct[position].phones[0].builtInStorageCapacity.toString()

                tvGB.text = "${listProduct[position].phones[0].builtInStorageCapacity} ${listProduct[position].phones[0].builtInStorageUnit}"

                tvCost.text = listProduct[position].phones[0].price.toString().formatStringNumber("#,###") + "đ"
//                tvCost.text = "37.500.000đ"

                val mamau = if (listProduct[position].phones[0].phoneColorName.toString() == "N/A") "Đen"
                else listProduct[position].phones[0].phoneColorName.toString()
                binding.tvMaMau.text = "$mamau "

                binding.tvBrand.text = listProduct[position].brandName
//
//                val cost = listProduct[position].cost.trim().lowercase().replace("đ", "").replace(".", "").replace(",", "").replace(" ", "")
//                val percent = listProduct[position].percent.trim().lowercase().replace("%", "").replace(" ", "")
//                var costPercent = (cost.toDouble() * percent.toDouble()) / 100
//                costPercent += cost.toDouble()
//                tvCostEmpty.text = "đ${(String.format("%.0f", costPercent.toFloat())).formatStringNumber("#,###")}"
                // khi click goi callback sp tuongung
                root.setOnClickListener {
                    callBackProduct?.callBackProduct(listProduct[position])
                }
            }
        }
    }
}