package ru.fm4m.exercisetechnique.checksex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.fm4m.exercisetechnique.R
import ru.fm4m.exercisetechnique.bodymain.BodyMainFragment
import ru.fm4m.exercisetechnique.model.Sex

class CheckSexFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_sex, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.buttonMan).setOnClickListener {
            findNavController().navigate(R.id.action_checkSexFragment_to_bodyMainFragment2,Bundle().apply {
                putSerializable(BodyMainFragment.ARG_SEX, Sex.MALE)
            })
        }
        view.findViewById<Button>(R.id.buttonWoman).setOnClickListener {
            findNavController().navigate(R.id.action_checkSexFragment_to_bodyMainFragment2,Bundle().apply {
                putSerializable(BodyMainFragment.ARG_SEX, Sex.FEMALE)
            })
        }


    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CheckSexFragment().apply {
            }
    }
}