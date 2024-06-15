package com.example.storeapplication.model

import com.google.gson.annotations.SerializedName

class LoginPost (
    @SerializedName("userName") var userName: String,
    @SerializedName("password") var password: String,
)