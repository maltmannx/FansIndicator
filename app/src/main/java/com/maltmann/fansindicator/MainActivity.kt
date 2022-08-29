package com.maltmann.fansindicator

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import com.google.android.material.color.DynamicColors
import java.security.Provider.Service

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Code starts here❤
        //==============================================

        // Enable Dynamic Color to MainActivity
        DynamicColors.applyIfAvailable(this)

        // Bind textview
        val textView = findViewById<TextView>(R.id.textView)
        var counter: Int = 0
        textView.text = counter.toString()
        // Data should be transmit to this textView.text.

        // Bind button0
        val button0 = findViewById<Button>(R.id.button0)
        // Set on click actions
        button0.setOnClickListener {
            textView.text = counter++.toString()
                // TODO(add vibrator)

        }
    }
}