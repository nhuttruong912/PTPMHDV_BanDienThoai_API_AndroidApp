package com.example.storeapplication

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.storeapplication.adapter.ColorAdapter
import com.example.storeapplication.adapter.StorageAdapter
import com.example.storeapplication.databinding.DialogBuyBinding
import com.example.storeapplication.databinding.ProductDetailActivityBinding
import com.example.storeapplication.extension.formatStringNumber
import com.example.storeapplication.extension.hide
import com.example.storeapplication.model.CartRoot
import com.example.storeapplication.model.PhoneDetailRoot
import com.example.storeapplication.model.Storage
import com.example.storeapplication.model.StorageRoot
import com.example.storeapplication.retrofit.APIService
import com.example.storeapplication.ultil.CallBack
import retrofit2.Call
import retrofit2.Callback
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import com.example.storeapplication.extension.getPref
import com.example.storeapplication.extension.showActivity
import com.example.storeapplication.model.CartPost
import retrofit2.Response

class ProductDetailActivity : BaseActivity<ProductDetailActivityBinding>(R.layout.product_detail_activity) {
    private var name = ""
    private var cost = ""
    private var brand = ""
    private var image = ""
    private var color = ""
    private var numberGb = ""
    private var Gb = ""
    private var phoneId = ""
    private var userId = ""

    private var product: PhoneDetailRoot? = null


    private val storageAdapter: StorageAdapter by lazy { StorageAdapter() }
    private val colorAdapter: ColorAdapter by lazy { ColorAdapter() }
    private var listStorage: MutableList<Storage> = arrayListOf()
    private var listMaMau: MutableList<com.example.storeapplication.model.Color> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProductDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindComponent()
        bindEvent()
    }

    override fun bindComponent() {
        name = intent.extras?.getString("NAME") ?: "Loading..."
        cost = intent.extras?.getString("COST") ?: "Loading..."
        brand = intent.extras?.getString("BRAND") ?: "Loading..."
        image = intent.extras?.getString("IMAGE") ?: "Loading..."
        color = intent.extras?.getString("COLOR") ?: "Loading..."
        numberGb = intent.extras?.getString("NUMBER_GB") ?: "Loading..."
        Gb = intent.extras?.getString("GB") ?: "Loading..."

        phoneId = intent.extras?.getString("PHONE_ID") ?: "Loading..."
        userId = getPref(this, "USERID", "").toString()

        setViewDetail()
    }

    override fun bindData() {
        getStorage()
        getColor()
    }

    private fun setViewDetail() {
        Glide.with(this@ProductDetailActivity).load(image).into(binding.ivProduct)
        binding.tvNameProduct.text = name
        binding.tvBrand.text = brand
        binding.tvDescriptionProduct.text = "Lý do chọn mua iPhone 15 tại Thế Giới Di Động\n" +
                "• Chất lượng sản phẩm: Thế Giới Di Động cam kết cung cấp sản phẩm iPhone 15 chính hãng và đảm bảo chất lượng. Điều này giúp bạn yên tâm về xuất xứ sản phẩm nhằm mang đến sự an tâm cho việc chọn mua sản phẩm.\n" +
                "\n" +
                "• Ưu đãi và khuyến mãi: Thế Giới Di Động thường xuyên có các chương trình khuyến mãi, giảm giá và tặng quà kèm, điều này giúp cho việc mua sắm iPhone 15 trở nên dễ dàng tiếp cận.\n" +
                "\n" +
                "• Hệ thống cửa hàng trải rộng: Thế Giới Di Động có mạng lưới cửa hàng trải rộng trên toàn quốc, dễ dàng tiếp cận và mua sắm. Bạn có thể kiểm tra sản phẩm trực tiếp và nhận sự hỗ trợ từ nhân viên tại cửa hàng.\n" +
                "\n" +
                "• Dịch vụ hậu mãi tốt: Thế Giới Di Động cung cấp dịch vụ hậu mãi chuyên nghiệp, bao gồm bảo hành, sửa chữa và hỗ trợ kỹ thuật. Điều này giúp bạn giải quyết mọi vấn đề kỹ thuật hoặc sự cố về sản phẩm dễ dàng.\n" +
                "\n" +
                "• Hệ thống trả góp linh hoạt: Thế Giới Di Động cung cấp các lựa chọn trả góp phù hợp với ngân sách của bạn, giúp bạn mua được sản phẩm mong muốn mà không cần thanh toán toàn bộ số tiền một lúc."

        binding.tvCost.text = cost.formatStringNumber("#,###") + "đ"
    }

    override fun bindEvent() {
        binding.ivBack.setOnClickListener { onBackPressed() }

        binding.btnBuy.setOnClickListener {
            showPopUpBuy()
        }
        binding.ivCart.setOnClickListener {
            postCart()
        }
    }

    private fun showPopUpBuy() {
        val dialog = Dialog(this@ProductDetailActivity)
        hideNavigation(dialog)
        val dialogBinding = DialogBuyBinding.inflate(LayoutInflater.from(this@ProductDetailActivity))
        dialog.setContentView(dialogBinding.root)
        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttributes = window?.attributes
        windowAttributes?.gravity = Gravity.CENTER

        dialogBinding.tvAgree.setOnClickListener {
            hideNavigation(dialog)
            val bundle = Bundle()
            bundle.putString("COST", cost)
            bundle.putString("NAME_PRODUCT", name)
            bundle.putString("PHONE_ID", phoneId)
            showActivity(this@ProductDetailActivity, BillActivity::class.java, bundle)
            dialog.dismiss()
        }

        dialogBinding.tvIgnore.setOnClickListener {
            hideNavigation(dialog)
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun getStorage() {
        val response = APIService.apiService.getStorage(phoneId.toInt())
        response.enqueue(object : Callback<StorageRoot> {
            override fun onResponse(call: Call<StorageRoot>, response: Response<StorageRoot>) {
                try {
                    response.body()?.storages?.toMutableList()?.let { listStorage.addAll(it) }
                    initRecyclerviewStorage()
                } catch (_: Exception) {
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "Get storage failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<StorageRoot>, t: Throwable) {
                Toast.makeText(
                    this@ProductDetailActivity,
                    "Get storage failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun initRecyclerviewStorage() {
        try {
            binding.recyclerView.apply {
                layoutManager = GridLayoutManager(this@ProductDetailActivity, 2)
                storageAdapter.addAll(listStorage, 0)
                adapter = storageAdapter
            }
        } catch (_: Exception) {}

        storageAdapter.callBackStorage(object : CallBack.CallBackStorage {
            override fun callBackStorage(storage: Storage, position: Int) {
                storageAdapter.checkSelect(position)
            }
        })
    }

    private fun getColor() {
        val response = APIService.apiService.getColor(phoneId.toInt(), 1)
        response.enqueue(object : Callback<List<com.example.storeapplication.model.Color>> {
            override fun onResponse(call: Call<List<com.example.storeapplication.model.Color>>, response: Response<List<com.example.storeapplication.model.Color>>) {
                try {
                    response.body()?.toMutableList()?.let { listMaMau.addAll(it) }
                    if (listMaMau.isNotEmpty()) {
                        initRecyclerviewMaMau()
                    } else {
                        binding.tvNameMau.hide()
                    }
                } catch (_: Exception) {
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "Get storage failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<com.example.storeapplication.model.Color>>, t: Throwable) {
                Toast.makeText(
                    this@ProductDetailActivity,
                    "Get storage failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun initRecyclerviewMaMau() {
        try {
            binding.recyclerViewMau.apply {
                layoutManager = GridLayoutManager(this@ProductDetailActivity, 2)
                colorAdapter.addAll(listMaMau, 0)
                adapter = colorAdapter
            }
        } catch (_: Exception) {}

        colorAdapter.callBackColor(object : CallBack.CallBackColor {
            override fun callBackColor(storage: com.example.storeapplication.model.Color, position: Int) {
                colorAdapter.checkSelect(position)
                binding.tvCost.text = storage.price.toString().formatStringNumber("#,###") + "đ"
            }
        })
    }

    private fun postCart() {

        val cartPost = CartPost(userId, phoneId.toInt())
        val response = APIService.apiService.postCart(cartPost)
        response.enqueue(object : Callback<CartRoot> {
            override fun onResponse(call: Call<CartRoot>, response: Response<CartRoot>) {
                try {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ProductDetailActivity,
                            "Add to cart successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        Toast.makeText(
                            this@ProductDetailActivity,
                            "Item exist in your cart",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (_: Exception) {
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "Cannot add to cart",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<CartRoot>, t: Throwable) {
                Toast.makeText(
                    this@ProductDetailActivity,
                    "Cannot add to cart",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun hideNavigation(dialog: Dialog) {
        dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}