package ru.fm4m.exercisetechnique.trainingview.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.fm4m.exercisetechnique.trainingview.R
import ru.fm4m.exercisetechnique.trainingview.ui.programs.AvailableProgramsFragment

class TrainingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate $this")
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