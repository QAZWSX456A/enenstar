package com.enen.decrypt

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat

class MainActivity : Activity() {
    private lateinit var viewFinder: PreviewView
    private lateinit var statusText: TextView
    private lateinit var injectBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frameLayout = FrameLayout(this)
        viewFinder = PreviewView(this).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            visibility = View.GONE 
        }
        frameLayout.addView(viewFinder)

        val uiLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#1A1A1A"))
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        }

        val titleText = TextView(this).apply {
            text = "ENEN 解密内核"
            setTextColor(Color.CYAN)
            textSize = 32f
            gravity = Gravity.CENTER
        }
        uiLayout.addView(titleText)

        statusText = TextView(this).apply {
            text = "状态：等待注入"
            setTextColor(Color.GRAY)
            textSize = 18f
            setPadding(0, 40, 0, 40)
            gravity = Gravity.CENTER
        }
        uiLayout.addView(statusText)

        injectBtn = Button(this).apply {
            text = "注入并启动相机"
            textSize = 20f
            setOnClickListener {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 10)
                }
            }
        }
        uiLayout.addView(injectBtn)

        frameLayout.addView(uiLayout)
        setContentView(frameLayout)
    }

    private fun startCamera() {
        statusText.text = "状态：正在唤醒天眼..."
        statusText.setTextColor(Color.GREEN)
        injectBtn.visibility = View.GONE
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also { it.setSurfaceProvider(viewFinder.surfaceProvider) }
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
                runOnUiThread {
                    viewFinder.visibility = View.VISIBLE
                    frameLayout.removeView(uiLayout)
                    Toast.makeText(this, "ENEN天眼已就绪！", Toast.LENGTH_SHORT).show()
                }
            } catch (exc: Exception) {
                runOnUiThread {
                    statusText.text = "启动失败: ${exc.message}"
                    statusText.setTextColor(Color.RED)
                    injectBtn.visibility = View.VISIBLE
                }
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "你拒绝了相机权限，无法注入！", Toast.LENGTH_LONG).show()
            }
        }
    }
}
