package com.example.storeapplication.model

import androidx.annotation.Keep
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
@TypeConverters(value = [Cheating::class])
data class Cheating(@SerializedName("cheating") var cheating: String = ""): Serializable