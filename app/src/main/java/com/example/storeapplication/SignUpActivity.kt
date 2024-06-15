package com.example.storeapplication

import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.storeapplication.databinding.ActivitySignUpBinding
import com.example.storeapplication.extension.hide
import com.example.storeapplication.extension.showActivity
import com.example.storeapplication.model.UserGet
import com.example.storeapplication.model.UserPost
import com.example.storeapplication.retrofit.APIService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    private var email: String = ""
    private var password: String = ""
    private var name: String = ""
    private var address: String = ""
    private var phone: String = ""
    private var firstName: String = ""

    override fun bindComponent() {
        binding.checkBoxSignUp.isChecked = true
    }

    override fun bindData() {}

    override fun bindEvent() {
        binding.tvHaveAnAccount.setOnClickListener { onBackPressed() }
        binding.btnPostSignUp.setOnClickListener {
            binding.frLoading.visibility = View.VISIBLE
            name = binding.edtName.text.toString().trim()
            email = binding.edtEmail.text.toString().trim()
            password = binding.edtPassword.text.toString().trim()
            address = binding.edtAddress.text.toString().trim()
            phone = binding.edtPhone.text.toString().trim()
            firstName = binding.edtFirstname.text.toString().trim()

            if (binding.checkBoxSignUp.isChecked) {
                if (name.isNotEmpty() &&
                    email.isNotEmpty() &&
                    password.isNotEmpty() &&
                    address.isNotEmpty() &&
                    phone.isNotEmpty() &&
                    firstName.isNotEmpty() &&
                    password.length >= 6 &&
                    Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    register(name, email, password, address, phone, firstName)
                } else {
                    if (name.isEmpty()) {
                        binding.edtName.error = "Name is required!"
                        binding.edtName.requestFocus()
                    } else if (email.isEmpty()) {
                        binding.edtEmail.error = "Email is required!"
                        binding.edtEmail.requestFocus()
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        binding.edtEmail.error = "Please provide valid email! @gmail.com?"
                        binding.edtEmail.requestFocus()
                    } else if (password.isEmpty()) {
                        binding.edtPassword.error = "Password is required!"
                        binding.edtPassword.requestFocus()
                    } else if (password.length < 6) {
                        binding.edtPassword.error = "Passwords must have at least 6 characters!"
                        binding.edtPassword.requestFocus()
                    } else if (address.isEmpty()) {
                        binding.edtAddress.error = "Address is required!"
                        binding.edtAddress.requestFocus()
                    } else if (phone.isEmpty()) {
                        binding.edtPhone.error = "Phone is required!"
                        binding.edtPhone.requestFocus()
                    }
                    binding.frLoading.hide()
                }
            } else {
                binding.frLoading.hide()
                Toast.makeText(
                    this@SignUpActivity,
                    "Please agree to the terms of the app!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun register(
        name: String,
        email: String,
        password: String,
        address: String,
        phone: String,
        firstName: String
    ) {
        val userPost = UserPost(
            name,
            email,
            password,
            address,
            phone,
            firstName
        )

        val response = APIService.apiService.registerUser(userPost)
        response.enqueue(object : Callback<UserGet> {
            override fun onResponse(call: Call<UserGet>, response: Response<UserGet>) {
                try {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Register success",
                            Toast.LENGTH_SHORT
                        ).show()
                        showActivity(this@SignUpActivity, LoginActivity::class.java)
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Username '${email}' is already taken.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (_: Exception) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Passwords must have at least one non alphanumeric character, least one lowercase ('a'-'z'), ('A'-'Z')",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                binding.frLoading.hide()
            }

            override fun onFailure(call: Call<UserGet>, t: Throwable) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Register failed",
                    Toast.LENGTH_SHORT
                ).show()
                binding.frLoading.hide()
            }

        })
    }
}