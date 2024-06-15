package com.example.storeapplication

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeapplication.adapter.CartAdapter
import com.example.storeapplication.databinding.CartActivityBinding
import com.example.storeapplication.databinding.DialogBuyBinding
import com.example.storeapplication.extension.getPref
import com.example.storeapplication.extension.hide
import com.example.storeapplication.extension.onClick
import com.example.storeapplication.extension.setPref
import com.example.storeapplication.extension.show
import com.example.storeapplication.extension.showActivity
import com.example.storeapplication.model.Item
import com.example.storeapplication.model.ItemRoot
import com.example.storeapplication.retrofit.APIService
import com.example.storeapplication.ultil.CallBack
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : BaseActivity<CartActivityBinding>(R.layout.cart_activity) {
    private var userId = ""
    private var listItemRoot: MutableList<Item> = arrayListOf()
    private val cartAdapter: CartAdapter by lazy { CartAdapter() }
    private var listOrderTemp: MutableList<Item> = ArrayList()

    override fun bindComponent() {
        userId = getPref(this, "USERID", "").toString()
    }

    override fun bindData() {
        getItemCart()
    }

    override fun bindEvent() {
        binding.ivBack.setOnClickListener { onBackPressed() }

        binding.loEmpty.onClick(1000) {
            showActivity(this@CartActivity, MainActivity::class.java)
            finishAffinity()
        }
        //event thay doi trang thai checkbox
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            cartAdapter.checkBox = isChecked
            binding.loBuyMany.isVisible = isChecked //hien thi layout mua nhieu
            cartAdapter.checkBox(isChecked)
            listOrderTemp.clear()
        }
    }
    //hthi ds sp trong cart
    private fun initRecyclerviewProduct() {
        try {
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
                cartAdapter.addAll(listItemRoot)
                adapter = cartAdapter

                setViewEmpty() //gọi ra neu rong thi hien layout rong
            }
        } catch (_: Exception) {}

        cartAdapter.callBackSelectMany(object : CallBack.CallBackSelectMany {
            override fun callBackSelectMany(storageRoot: Item, position: Int) {
                if (binding.checkBox.isChecked) {
                    addManyItemProduct(storageRoot)
                } else {
                    showPopUpBuyAItem(storageRoot)
                }
            }
        })
    }
    //hop thoai xac nhan mua
    private fun showPopUpBuyAItem(product: Item) {
        val dialog = Dialog(this@CartActivity)
        hideNavigation(dialog)
        val dialogBinding = DialogBuyBinding.inflate(LayoutInflater.from(this@CartActivity))
        dialog.setContentView(dialogBinding.root)
        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttributes = window?.attributes
        windowAttributes?.gravity = Gravity.CENTER

        dialogBinding.tvAgree.setOnClickListener {
            hideNavigation(dialog)
            buyOneItemProduct(product)
            dialog.dismiss()
        }

        dialogBinding.tvIgnore.setOnClickListener {
            hideNavigation(dialog)
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }
    //mua 1 sp
    private fun buyOneItemProduct(item: Item) {
        binding.frLoading.show()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.frLoading.hide()
            val bundle = Bundle()
            bundle.putString("COST", item.price.toString())
            bundle.putString("NAME_PRODUCT", item.phoneName)
            bundle.putString("PHONE_ID", item.phoneOptionId.toString())
            showActivity(this@CartActivity, BillActivity::class.java, bundle)
        }, 1000)
    }

    private fun addManyItemProduct(item: Item) {
        val order = Item(
            phoneOptionId = item.phoneOptionId,
            phoneName = item.phoneName,
            phoneColor = item.phoneColor,
            stogare = item.stogare,
            imageUrl = item.imageUrl,
            price = item.price,
            quantity = item.quantity,
        )
        listOrderTemp.add(order)
    }
    //get thong tin gio hang
    private fun getItemCart() {
        val response = APIService.apiService.getCart(userId)
        response.enqueue(object : Callback<ItemRoot> {
            override fun onResponse(call: Call<ItemRoot>, response: Response<ItemRoot>) {
                try {
                    Log.d("iiiiii", Gson().toJson(response.body()))
                    response.body()?.items?.toMutableList()?.let { listItemRoot.addAll(it) }

                    initRecyclerviewProduct()

                } catch (_: Exception) {
                    Toast.makeText(
                        this@CartActivity,
                        "Get list item cart failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ItemRoot>, t: Throwable) {
                Toast.makeText(
                    this@CartActivity,
                    "Get list cart failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    //neu ds rong thi hien layout rong
    private fun setViewEmpty() {
        binding.loEmpty.isVisible = listItemRoot.isEmpty()
    }

    private fun hideNavigation(dialog: Dialog) {
        dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}