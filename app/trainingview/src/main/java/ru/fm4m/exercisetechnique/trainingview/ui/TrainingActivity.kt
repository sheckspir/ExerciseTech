package ru.fm4m.exercisetechnique.trainingview.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.fm4m.exercisetechnique.trainingview.R
import ru.fm4m.exercisetechnique.trainingview.ui.programs.AvailableProgramsFragment

class TrainingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("TAG" , "before onCreate $this")
        super.onCreate(savedInstanceState)
        Log.d("TAG" , "onCreate $this")
        setContentView(R.layout.activity_training)

        var firstFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
        Log.d("TAG", "first fragment = $firstFragment")
        if (firstFragment == null || firstFragment !is AvailableProgramsFragment) {
            firstFragment = AvailableProgramsFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, firstFragment)
                .commit()
        }
    }
}