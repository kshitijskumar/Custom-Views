package com.example.customviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customviews.canvas.CupView

class MainActivity : AppCompatActivity() {

    private val cupView by lazy {
        findViewById<CupView>(R.id.cup)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cupView.setOnClickListener {
            cupView.showBubbles()
        }
    }
}