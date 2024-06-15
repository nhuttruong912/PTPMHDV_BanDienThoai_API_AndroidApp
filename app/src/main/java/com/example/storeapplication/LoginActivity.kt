package com.example.storeapplication

import android.content.Intent
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.storeapplication.databinding.ActivityLoginBinding
import com.example.storeapplication.extension.hide
import com.example.storeapplication.extension.setPref
import com.example.storeapplication.extension.show
import com.example.storeapplication.extension.showActivity
import com.example.storeapplication.model.LoginGet
import com.example.storeapplication.model.LoginPost
import com.example.storeapplication.retrofit.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private var username: String = ""
    private var password: String = ""

    override fun bindComponent() {
        binding.checkBoxLogin.isChecked = true
    }

    override fun bindData() {}

    override fun bindEvent() {
        binding.tvDontHaveAnAccount.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.btnGetLogin.setOnClickListener {
            binding.frLoading.show()
            username = binding.edtEmail.text.toString().trim()
            password = binding.edtPassword.text.toString().trim()
            if (binding.checkBoxLogin.isChecked) {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    login(username, password)
                } else {
                    if (username.isEmpty()) {
                        binding.edtEmail.error = "Email is required!"
                        binding.edtEmail.requestFocus()
                    } else if (password.isEmpty()) {
                        binding.edtPassword.error = "Password is required!"
                        binding.edtPassword.requestFocus()
                    } else if (password.length < 6) {
                        binding.edtPassword.error = "Passwords must have at least 6 characters!"
                        binding.edtPassword.requestFocus()
                    }
                    binding.frLoading.visibility = View.GONE
                }
            } else {
                binding.frLoading.visibility = View.GONE
                Toast.makeText(
                    this@LoginActivity,
                    "Please agree to the terms of the app!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun login(email: String, password: String) {
        val loginPost = LoginPost(email, password,)
        APIService.apiService.login(loginPost)
            .enqueue(object : Callback<LoginGet> {
                override fun onResponse(call: Call<LoginGet>, response: Response<LoginGet>) {
                    if (response.isSuccessful) {
                        setPref(this@LoginActivity, "USERID", response.body()?.userId.toString())
                        setPref(this@LoginActivity, "NAME", response.body()?.userName.toString())
                        setPref(this@LoginActivity, "EMAIL", response.body()?.email.toString())
                        setPref(this@LoginActivity, "ADDRESS", response.body()?.address.toString())
                        setPref(this@LoginActivity, "PHONE", response.body()?.phoneNumber.toString())

                        Toast.makeText(
                            this@LoginActivity,
                            "SignIn success",
                            Toast.LENGTH_SHORT
                        ).show()
                        showActivity(this@LoginActivity, MainActivity::class.java)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Username or Password incorrect",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.frLoading.hide()
                }

                override fun onFailure(call: Call<LoginGet>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "SignIn failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.frLoading.hide()
                }
            })
    }

}