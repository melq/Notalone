package com.melq.notalone

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.ColorUtils
import java.lang.Float.max

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBarColor = Color.parseColor("#FF66BB6A")
        this.window.statusBarColor =actionBarColor.actionBarColorToStatusBarColor()
    }

    private fun Int.actionBarColorToStatusBarColor(): Int {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(this, hsl)
        hsl[0] = max(0F, hsl[0] - 6)
        hsl[2] = max(0F, hsl[2] - 0.09F)
        return ColorUtils.HSLToColor(hsl)
    }
}