package com.example.storeapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeapplication.adapter.ProductAdapter
import com.example.storeapplication.adapter.ProductHotAdapter
import com.example.storeapplication.databinding.ActivityMainBinding
import com.example.storeapplication.extension.showActivity
import com.example.storeapplication.model.Phone
import com.example.storeapplication.model.PhoneDetailRoot
import com.example.storeapplication.model.UserGet
import com.example.storeapplication.model.UserPost
import com.example.storeapplication.retrofit.APIService
import com.example.storeapplication.ultil.CallBack
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private var listPhone: MutableList<Phone> = arrayListOf()
    private val productAdapter: ProductAdapter by lazy { ProductAdapter() }
    private val productHOTAdapter: ProductHotAdapter by lazy { ProductHotAdapter() }
    private var listPhoneDetail: MutableList<PhoneDetailRoot> = arrayListOf()

    override fun bindComponent() {
        getPhone()
    }

    override fun bindData() {

    }

    override fun bindEvent() {
        binding.ivCart.setOnClickListener {
            showActivity(this@MainActivity, CartActivity::class.java)
        }
    }

    private fun getPhone() {
        val response = APIService.apiService.allPhoneActive
        response.enqueue(object : Callback<List<Phone>> {
            override fun onResponse(call: Call<List<Phone>>, response: Response<List<Phone>>) {
                try {
                    response.body()?.toMutableList()?.let { listPhone.addAll(it) }

                    getPhoneDetail()

                } catch (_: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Get list phone failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Phone>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Get list phone failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getPhoneDetail() {
        for (i in 0 until listPhone.size) {
            val response = APIService.apiService.getPhoneDetail(listPhone[i].name)
            response.enqueue(object : Callback<List<PhoneDetailRoot>> {
                override fun onResponse(call: Call<List<PhoneDetailRoot>>, response: Response<List<PhoneDetailRoot>>) {
                    try {
                        response.body()?.toMutableList()?.let { listPhoneDetail.addAll(it) }

                        for (i in 0 until listPhoneDetail.size) {
                            if (listPhoneDetail[i].phones[0].builtInStorageUnit.toString() == "N/A") {
                                listPhoneDetail.removeAt(i)
                            }
                        }
                    } catch (_: Exception) {}
                    initRecyclerviewProduct()
                    initRecyclerviewProductHot()
                }

                override fun onFailure(call: Call<List<PhoneDetailRoot>>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "FAILED",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    private fun initRecyclerviewProduct() {
        try {
            binding.recyclerView.apply {
                layoutManager = GridLayoutManager(this@MainActivity, 2)
                productAdapter.addAll(listPhoneDetail)
                adapter = productAdapter
            }
        } catch (_: Exception) {}

        productAdapter.callBackProduct(object : CallBack.CallBackProduct {
            override fun callBackProduct(product: PhoneDetailRoot) {
                val bundle = Bundle()
                bundle.putString("NAME", product.phones[0].phoneName)
                bundle.putString("COST", product.phones[0].price.toString())
                bundle.putString("BRAND", product.brandName)
                bundle.putString("COLOR", product.phones[0].phoneColorName)

                bundle.putString("IMAGE", "https://imgs.sundaymore.com/wp-content/uploads/2023/06/img_2036_3563603564919217acc2f.jpg")
                bundle.putString("NUMBER_GB", product.phones[0].builtInStorageCapacity.toString())
                bundle.putString("GB", product.phones[0].builtInStorageUnit.toString())
                bundle.putString("PHONE_ID", product.phones[0].phoneId.toString())
                showActivity(this@MainActivity, ProductDetailActivity::class.java, bundle)
            }
        })
    }

    private fun initRecyclerviewProductHot() {
        try {
            binding.recyclerViewHot.apply {
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                productHOTAdapter.addAll(listPhoneDetail)
                adapter = productHOTAdapter
            }
        } catch (_: Exception) {}

        productHOTAdapter.callBackProduct(object : CallBack.CallBackProduct {
            override fun callBackProduct(product: PhoneDetailRoot) {
                val bundle = Bundle()
                bundle.putString("NAME", product.phones[0].phoneName)
                bundle.putString("COST", product.phones[0].price.toString())
                bundle.putString("BRAND", product.brandName)
                bundle.putString("COLOR", product.phones[0].phoneColorName)
                bundle.putString("IMAGE", "https://cdn.tgdd.vn/Products/Images/42/281570/TimerThumb/iphone-15-(6).jpg")
                bundle.putString("NUMBER_GB", product.phones[0].builtInStorageCapacity.toString())
                bundle.putString("GB", product.phones[0].builtInStorageUnit.toString())
                bundle.putString("PHONE_ID", product.phones[0].phoneId.toString())
                showActivity(this@MainActivity, ProductDetailActivity::class.java, bundle)
            }
        })
    }
}