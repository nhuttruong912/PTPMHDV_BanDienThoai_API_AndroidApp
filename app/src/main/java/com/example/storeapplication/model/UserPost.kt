package com.example.storeapplication.model

import com.google.gson.annotations.SerializedName

class UserPost (
    @SerializedName("username") var username: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("address") var address: String,
    @SerializedName("phoneNumber") var phoneNumber: String,
    @SerializedName("firstName") var firstName: String,
)