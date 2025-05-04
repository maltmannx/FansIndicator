package com.maltmann.fansindicator

import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.github.kittinunf.fuel.Fuel
import com.google.android.material.color.DynamicColors
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//TODO: Check permission everytime when activity starts.
//TODO: Set delay after get JSON from web. May combine with short and long press the button.

class MainActivity : AppCompatActivity() {

    // history pattern
    private val historyList = mutableListOf<HistoryRecord>()
    private lateinit var historyAdapter: HistoryAdapter

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable Dynamic Color to MainActivity
        DynamicColors.applyToActivityIfAvailable(this)
        // Must set dynamic color before content create
        setContentView(R.layout.activity_main)

        // Code starts here❤
        //==============================================


        // Initial history recyclerview
        val historyRecyclerView = findViewById<RecyclerView>(R.id.historyRecyclerView)
        historyAdapter = HistoryAdapter(historyList)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter

        // Bind textview
        val textView = findViewById<TextView>(R.id.textView)
        // Data should be transmit to this textView.text.

        // Bind button0
        val button0 = findViewById<Button>(R.id.button0)

        // Bind input textview.
        val inputTextView = findViewById<TextInputEditText>(R.id.inputTextView)

        // Set on click actions
        button0.setOnClickListener {
            val rawInput = inputTextView.text.toString()
            val filteredUid = rawInput.replace("[^0-9]".toRegex(), "")
            if (filteredUid.isNotEmpty()) {
                // Auto filter UID after submit.
                inputTextView.setText(filteredUid)
                // Submit button pressed.
                fetchFansData(filteredUid, textView, button0)
            } else {
                button0.performHapticFeedback(HapticFeedbackConstants.REJECT)
                textView.text = "Please enter a valid UID"
            }
        }
    }

    // Get fans number from bilbili site.
    private fun fetchFansData(uid: String, textView: TextView, button: Button) {
        Fuel.get("https://api.bilibili.com/x/relation/stat?vmid=${uid}&jsonp=jsonp")
            .response { _, response, result ->
                val (bytes, error) = result
                if (error == null && bytes != null) {
                    runOnUiThread {
                        button.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                        val follower = JSON.parseObject(String(bytes))
                            .getJSONObject("data")
                            .getString("follower")
                        textView.text = follower

                        // Add to history list
                        val timeStamp = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
                            .format(Date())
                        historyList.add(HistoryRecord(uid, follower, timeStamp))
                        historyAdapter.notifyItemInserted(historyList.size - 1)
                    }
                } else {
                    runOnUiThread {
                        button.performHapticFeedback(HapticFeedbackConstants.REJECT)
                        textView.text = "ERROR: ${error?.message}"
                    }
                }
            }
    }

    // History data class
    data class HistoryRecord(
        val uid: String,
        val fans: String,
        val time: String
    )

    // RecyclerView adapter
    class HistoryAdapter(private val historyList: List<HistoryRecord>) :
        RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val uidText: TextView = view.findViewById(R.id.historyUid)
            val fansText: TextView = view.findViewById(R.id.historyFans)
            val timeText: TextView = view.findViewById(R.id.historyTime)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = historyList[position]
            holder.uidText.text = "UID: ${record.uid}"
            holder.fansText.text = "Fans: ${record.fans}"
            holder.timeText.text = record.time
        }

        override fun getItemCount() = historyList.size
    }
}