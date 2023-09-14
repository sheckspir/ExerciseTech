package ru.fm4m.exercisetechnique.techview.checksex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_check_sex.view.*
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techview.bodymain.BodyMainFragment
import ru.fm4m.exercisetechnique.techdomain.data.Sex

class ChooseFirstFragment : Fragment(R.layout.fragment_check_sex) {

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

        view.buttonSearch.setOnClickListener{
            findNavController().navigate(R.id.action_checkSexFragment_to_searchVideosFragment)
        }

        view.buttonNewProgram.setOnClickListener{
            findNavController().navigate(R.id.action_checkSexFragment_to_newProgramFragment)
        }


    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChooseFirstFragment().apply {
            }
    }
}