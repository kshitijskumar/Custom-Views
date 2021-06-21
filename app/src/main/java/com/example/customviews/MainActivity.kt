package com.example.customviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.customviews.canvas.CupView
import com.example.customviews.canvas.TimerView
import java.lang.Exception

class MainActivity : AppCompatActivity() {

//    private val cupView by lazy {
//        findViewById<CupView>(R.id.cup)
//    }

    private val timerView by lazy {
        findViewById<TimerView>(R.id.timer)
    }
    private val timerText by lazy {
        findViewById<EditText>(R.id.etTime)
    }
    private val btnStart by lazy {
        findViewById<Button>(R.id.btnStart)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnStart.setOnClickListener {
            try {
                val timeInMillis = timerText.text.toString().toLong() * 1000
                timerView.startTimer(timeInMillis)
            } catch (e: Exception) {
                Toast.makeText(this, "Please enter time", Toast.LENGTH_LONG).show()
            }
        }
    }
}