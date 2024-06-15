package com.example.storeapplication

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.storeapplication.databinding.BillActivityBinding
import com.example.storeapplication.databinding.DialogSuccessBinding
import com.example.storeapplication.extension.formatStringNumber
import com.example.storeapplication.extension.getPref
import com.example.storeapplication.extension.hide
import com.example.storeapplication.extension.setPref
import com.example.storeapplication.extension.show
import com.example.storeapplication.extension.showActivity
import com.example.storeapplication.model.CartPost
import com.example.storeapplication.model.CartRoot
import com.example.storeapplication.model.LoginPost
import com.example.storeapplication.retrofit.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BillActivity : BaseActivity<BillActivityBinding>(R.layout.bill_activity) {
    private var nameUser = ""
    private var email = ""
    private var phone = ""
    private var address = ""
    private var cost = ""
    private var nameProduct = ""
    private var userId = ""
    private var phoneId = ""

    override fun bindComponent() {
        userId = getPref(this, "USERID", "").toString()
        phoneId = intent.extras?.getString("PHONE_ID") ?: "22"

        nameUser = getPref(this@BillActivity, "NAME", "Unknown").toString()
        email = getPref(this@BillActivity, "EMAIL", "Unknown").toString()
        phone = getPref(this@BillActivity, "PHONE", "Unknown").toString()
        address = getPref(this@BillActivity, "ADDRESS", "Unknown").toString()
        cost = intent.extras?.getString("COST") ?: "Loading..."
        nameProduct = intent.extras?.getString("NAME_PRODUCT") ?: "Loading..."

        binding.tvDate.text = "${timeCurrent()} ${dateCurrent()}"
        binding.tvNameCustomer.text = "Khách hàng: $nameUser"
        binding.tvPhoneCustomer.text = "Số điện thoại: $phone"
        binding.tvAddressCustomer.text = "Đ/c: $address"

        binding.tvNumber.text = "1"
        binding.tvTotal.text = "đ" + (cost.toString().replace(".", "").replace(",", "").toInt()).toString().formatStringNumber("#,###")
        binding.tvPayment.text = "đ" + (cost.toString().replace(".", "").replace(",", "").toInt()).toString().formatStringNumber("#,###")
        binding.tvName.text = nameProduct


    }

    override fun bindData() {}

    override fun bindEvent() {
        binding.ivBack.setOnClickListener { onBackPressed() }

        binding.cvTechcombank.setOnClickListener {
            binding.frLoading.show()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.frLoading.hide()
                val bundle = Bundle()
                bundle.putString("COST", cost)
                bundle.putString("NAME_PRODUCT", nameProduct)
                bundle.putString("PHONE_ID", phoneId)
                showActivity(this@BillActivity, TechcombankActivity::class.java, bundle)
            }, 1000)
        }

        binding.cvPayment.setOnClickListener {
            binding.frLoading.show()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.frLoading.hide()
//                postCard()
                showPopUpSuccess()
            }, 1000)
        }
    }

    private fun showPopUpSuccess() {
        val dialog = Dialog(this@BillActivity)
        hideNavigation(dialog)
        val dialogBinding = DialogSuccessBinding.inflate(LayoutInflater.from(this@BillActivity))
        dialog.setContentView(dialogBinding.root)
        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttributes = window?.attributes
        windowAttributes?.gravity = Gravity.CENTER

        dialogBinding.ivTechcombank.setImageDrawable(ContextCompat.getDrawable(this@BillActivity, R.drawable.logo))
        dialogBinding.tvTitle.text = "Đặt hàng thành công! Vui lòng kiểm tra đơn hàng đã đặt."
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            hideNavigation(dialog)
            showActivity(this@BillActivity, MainActivity::class.java)
            finishAffinity()
        }, 1000)

        dialog.setCancelable(false)
        dialog.show()
    }



    private fun hideNavigation(dialog: Dialog) {
        dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private fun dateCurrent(): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    private fun timeCurrent(): String {
        return SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(Date())
    }
}