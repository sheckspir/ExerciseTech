package ru.fm4m.exercisetechnique

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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