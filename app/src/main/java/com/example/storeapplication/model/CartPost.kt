package com.example.storeapplication.model

import com.google.gson.annotations.SerializedName

class CartPost (
    @SerializedName("userId") var userId: String,
    @SerializedName("phoneOptionId") var phoneOptionId: Int,
)