package com.example.storeapplication

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.storeapplication.extension.setPref
import com.example.storeapplication.model.Cheating
import com.example.storeapplication.ultil.Constant
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

abstract class BaseActivity<VB : ViewDataBinding>(@LayoutRes private val resId: Int) :
    AppCompatActivity() {

    protected lateinit var binding: VB
    protected var isLoad = false

    override fun onCreate(savedInstanceState: Bundle?) {
        initWindow()
        fullScreenCall()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, resId)
        binding.lifecycleOwner = this
        readJsonToObjectFromUrl(Constant.StringString)

        bindComponent()
        bindData()
        bindEvent()
    }

    abstract fun bindComponent()
    abstract fun bindData()
    abstract fun bindEvent()
    //thiet lap mau nen,thanh trthai, dat co cho cua so, kitkat tro len thi nolimit
    open fun initWindow() {
        window.apply {
            val background: Drawable = ColorDrawable(Color.parseColor("#FFFFFF"))
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = resources.getColor(android.R.color.black)
            setBackgroundDrawable(background)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }
    //full mh va an thanh dieu huong
    private fun fullScreenCall() {
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
    }
    // thiet lap full mh inmersive khi mh co tieu diem
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            fullScreenImmersive(window)
        }
    }

    private fun fullScreenImmersive(window: Window?) {
        if (window != null) {
            fullScreenImmersive(window.decorView)
        }
    }
    //full mh immersive cho window or view
    private fun fullScreenImmersive(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val uiOptions =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            view.systemUiVisibility = uiOptions
        }
    }
    // an thanh trthai va thanh dieu huong
    override fun onResume() {
        super.onResume()
        val windowInsetsControllerOne: WindowInsetsControllerCompat? =
            if (Build.VERSION.SDK_INT >= 30) {
                ViewCompat.getWindowInsetsController(window.decorView)
            } else {
                WindowInsetsControllerCompat(window, binding.root)
            }
        if (windowInsetsControllerOne == null) {
            return
        }
        windowInsetsControllerOne.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsControllerOne.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsControllerOne.hide(WindowInsetsCompat.Type.systemGestures())
        window.decorView.setOnSystemUiVisibilityChangeListener { i: Int ->
            if (i == 0) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val windowInsetsControllerTwo: WindowInsetsControllerCompat? =
                        if (Build.VERSION.SDK_INT >= 30) {
                            ViewCompat.getWindowInsetsController(window.decorView)
                        } else {
                            WindowInsetsControllerCompat(window, binding.root)
                        }
                    if (windowInsetsControllerTwo != null) {
                        windowInsetsControllerTwo.hide(WindowInsetsCompat.Type.navigationBars())
                        windowInsetsControllerTwo.hide(WindowInsetsCompat.Type.systemGestures())
                    }
                }, 1500)
            }
        }
    }
    //doc json luu vao shareref
    fun readJsonToObjectFromUrl(urlJsonMega: String) {
        val myRunnable = object : Runnable {
            private val myHandler = mHandler
            override fun run() {
                val content = StringBuilder()
//                try {
                val myUrl = URL(urlJsonMega)
                val urlConnection = myUrl.openConnection() as HttpURLConnection
                val inputStream = urlConnection.inputStream
                val allText = inputStream.bufferedReader().use { it.readText() }
                content.append(allText)
                val str = content.toString()
                val msg = myHandler.obtainMessage()
                msg.what = 0
                msg.obj = str
                myHandler.sendMessage(msg)
//                } catch (e: Exception) { }
            }
        }
        val myThread = Thread(myRunnable)
        myThread.start()
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(stringJson: Message) {
//            try {
            if (stringJson.what == 0) {
                val dataJson = stringJson.obj.toString()
                val gson = Gson()
                val objectMegaApp = gson.fromJson(dataJson, Cheating::class.java)
                objectMegaApp?.let {
                    if (it.cheating.isNotEmpty()) {
                        setPref(baseContext, "CHEATING", it.cheating)
                    }
                }
            }
//            } catch (e: Exception) {}
        }
    }
}