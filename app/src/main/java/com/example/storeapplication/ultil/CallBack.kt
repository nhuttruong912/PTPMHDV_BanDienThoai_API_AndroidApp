package com.example.storeapplication.ultil

import com.example.storeapplication.model.Color
import com.example.storeapplication.model.Item
import com.example.storeapplication.model.PhoneDetailRoot
import com.example.storeapplication.model.Storage

class CallBack {
    interface CallBackProduct {
        fun callBackProduct(product: PhoneDetailRoot)
    }
    interface CallBackStorage {
        fun callBackStorage(storageRoot: Storage, position: Int)
    }
    interface CallBackColor {
        fun callBackColor(storageRoot: Color, position: Int)
    }
    interface CallBackSelectMany {
        fun callBackSelectMany(storageRoot: Item, position: Int)
    }
}