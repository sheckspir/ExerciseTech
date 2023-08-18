package ru.fm4m.exercisetechnique

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.fm4m.exercisetechnique.trainingview.ui.UserTrainingActivity

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

    fun startNewActivity() {
        startActivity(Intent(applicationContext, UserTrainingActivity::class.java))
//        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}