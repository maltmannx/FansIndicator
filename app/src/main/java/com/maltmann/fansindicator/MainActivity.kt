package com.maltmann.fansindicator

import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.fastjson.JSON
import com.github.kittinunf.fuel.Fuel
import com.google.android.material.color.DynamicColors
import com.google.android.material.textfield.TextInputEditText

//TODO: Check permission everytime when activity starts.
//TODO: Set delay after get JSON from web. May combine with short and long press the button.


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Code starts here❤
        //==============================================

        // Enable Dynamic Color to MainActivity
        DynamicColors.applyToActivityIfAvailable(this)

        // Bind textview
        val textView = findViewById<TextView>(R.id.textView)
        // Data should be transmit to this textView.text.

        // Bind button0
        val button0 = findViewById<Button>(R.id.button0)

        // Bind input textview.
        val inputTextView = findViewById<TextInputEditText>(R.id.inputTextView)

        val uid = inputTextView.text

        // Set on click actions
        button0.setOnClickListener {

            // Get fans number from bilbili site.
            Fuel.get("https://api.bilibili.com/x/relation/stat?vmid=${uid}&jsonp=jsonp")
                .response { request, response, result ->
                    println(request)
                    println(response)
                    val (bytes) = result
                    if (bytes != null) {
                        // Haptic feedback.
                        button0.performHapticFeedback(HapticFeedbackConstants.CONFIRM)

                        val follower = JSON.parseObject(String(bytes))
                            .getJSONObject("data")
                            .getString("follower")

                        textView.text = follower

                        println("[response bytes] ${String(bytes)}")
                        println(String(bytes))
                        println(follower)

                    } else {
                        button0.performHapticFeedback(HapticFeedbackConstants.REJECT)
                        textView.text = "ERROR"
                    }
                }



        }


    }


}

