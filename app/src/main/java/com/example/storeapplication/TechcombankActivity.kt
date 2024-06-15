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
import com.example.storeapplication.databinding.DialogSuccessBinding
import com.example.storeapplication.databinding.TechcombankActivityBinding
import com.example.storeapplication.extension.formatStringNumber
import com.example.storeapplication.extension.getPref
import com.example.storeapplication.extension.hide
import com.example.storeapplication.extension.setPref
import com.example.storeapplication.extension.show
import com.example.storeapplication.extension.showActivity
import com.example.storeapplication.model.CartPost
import com.example.storeapplication.model.CartRoot
import com.example.storeapplication.retrofit.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TechcombankActivity : BaseActivity<TechcombankActivityBinding>(R.layout.techcombank_activity) {
    private var email = ""
    private var phone = ""
    private var address = ""
    private var nameUser = ""
    private var cost = ""
    private var nameProduct = ""
    private var userId = ""
    private var phoneId = ""

    override fun bindComponent() {
        userId = getPref(this, "USERID", "").toString()
        phoneId = intent.extras?.getString("PHONE_ID") ?: "22"

        nameUser = getPref(this@TechcombankActivity, "NAME", "Unknown").toString()
        email = getPref(this@TechcombankActivity, "EMAIL", "Unknown").toString()
        phone = getPref(this@TechcombankActivity, "PHONE", "Unknown").toString()
        address = getPref(this@TechcombankActivity, "ADDRESS", "Unknown").toString()
        cost = intent.extras?.getString("COST") ?: "Loading..."
        nameProduct = intent.extras?.getString("NAME_PRODUCT") ?: "Loading..."

        binding.tvContentVnd.text = (cost.replace(".", "").replace(",", "").toInt()).toString().formatStringNumber("#,###").replace(".", ",")
        binding.tvMessage.text = nameUser + " chuyen khoan"
    }

    override fun bindData() {}

    override fun bindEvent() {
        binding.btnPayment.setOnClickListener {
            binding.frLoading.show()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.frLoading.hide()

                showPopUpSuccess()
            }, 1000)
        }
    }

    private fun showPopUpSuccess() {
        val dialog = Dialog(this@TechcombankActivity)
        hideNavigation(dialog)
        val dialogBinding = DialogSuccessBinding.inflate(LayoutInflater.from(this@TechcombankActivity))
        dialog.setContentView(dialogBinding.root)
        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttributes = window?.attributes
        windowAttributes?.gravity = Gravity.CENTER

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            hideNavigation(dialog)
            showActivity(this@TechcombankActivity, MainActivity::class.java)
            finishAffinity()
        }, 1000)

        dialog.setCancelable(false)
        dialog.show()
    }

    private fun hideNavigation(dialog: Dialog) {
        dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

}