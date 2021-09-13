package com.example.exercisetechnique.checksex

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.exercisetechnique.R
import com.example.exercisetechnique.body.BodyFragment
import com.example.exercisetechnique.model.Sex
import com.example.exercisetechnique.model.Side

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
            findNavController().navigate(R.id.action_checkSexFragment_to_bodyFragment,Bundle().apply {
                putSerializable(BodyFragment.ARG_SEX, Sex.MALE)
                putSerializable(BodyFragment.ARG_SIDE, Side.FRONT)
            })
        }
        view.findViewById<Button>(R.id.buttonWoman).setOnClickListener {
            findNavController().navigate(R.id.action_checkSexFragment_to_bodyFragment,Bundle().apply {
                putSerializable(BodyFragment.ARG_SEX, Sex.FEMALE)
                putSerializable(BodyFragment.ARG_SIDE, Side.FRONT)
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