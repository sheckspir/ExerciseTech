package com.example.exercisetechnique

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(600)
    }

    override fun onResume() {
        super.onResume()
        startNewActivity()
        finish()
    }

    fun startNewActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}