package com.example.storeapplication.model

import com.google.gson.annotations.SerializedName

class LoginGet (
    @SerializedName("userId") var userId: String,
    @SerializedName("userName") var userName: String,
    @SerializedName("email") var email: String,
    @SerializedName("address") var address: String,
    @SerializedName("phoneNumber") var phoneNumber: String,
    @SerializedName("firstName") var firstName: String,
    @SerializedName("token") var token: String,
)