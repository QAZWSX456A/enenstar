package com.enen.decrypt

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val textView = TextView(this).apply {
            text = "一恩一恩解密\n\n基础编译测试成功！\n下一步：加相机和画框！"
            textSize = 24f
            setPadding(50, 300, 50, 50)
        }
        setContentView(textView)
    }
}
