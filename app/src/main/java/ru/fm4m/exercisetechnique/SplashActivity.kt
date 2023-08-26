package ru.fm4m.exercisetechnique

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.fm4m.exercisetechnique.trainingview.ui.TrainingActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(100)
    }

    override fun onResume() {
        super.onResume()
        startNewActivity()
        finish()
    }

    private fun startNewActivity() {
//        startActivity(Intent(applicationContext, TrainingActivity::class.java))
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}