package com.enen.decrypt

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    // 状态标记
    private var isCameraGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. 创建外挂主界面的布局 (垂直排列)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#1A1A1A")) // 黑灰色背景
        }

        // 2. 标题文字
        val titleText = TextView(this).apply {
            text = "ENEN 解密内核"
            setTextColor(Color.CYAN)
            textSize = 32f
            gravity = Gravity.CENTER
        }
        layout.addView(titleText)

        // 3. 状态提示
        val statusText = TextView(this).apply {
            text = "状态：未注入"
            setTextColor(Color.GRAY)
            textSize = 18f
            setPadding(0, 40, 0, 40)
        }
        layout.addView(statusText)

        // 4. 核心按钮：注入 / 启动
        val injectBtn = Button(this).apply {
            text = "注入并启动相机"
            textSize = 20f
            setOnClickListener {
                // 检查相机权限
                if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                    isCameraGranted = true
                    statusText.text = "状态：已注入，正在启动相机..."
                    statusText.setTextColor(Color.GREEN)
                    // 这里先写个Toast模拟，下一步咱们接真正的相机启动
                    Toast.makeText(this@MainActivity, "权限已获取，准备启动相机！", Toast.LENGTH_SHORT).show()
                    
                    // TODO: 下一步在这里调用 startCamera()
                } else {
                    // 没有权限就申请
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CAMERA), 10)
                }
            }
        }
        layout.addView(injectBtn)

        // 把布局设为当前界面的主视图
        setContentView(layout)
    }

    // 处理权限申请结果
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isCameraGranted = true
                Toast.makeText(this, "相机权限已获取！请再次点击注入。", Toast.LENGTH_LONG).show()
                // 权限下来后，用户可以再点一次“注入”按钮，或者咱们自动帮它跳转
            } else {
                Toast.makeText(this, "你拒绝了相机权限，无法注入！", Toast.LENGTH_LONG).show()
            }
        }
    }
}
