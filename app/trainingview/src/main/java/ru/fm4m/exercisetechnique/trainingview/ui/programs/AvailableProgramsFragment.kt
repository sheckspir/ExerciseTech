package ru.fm4m.exercisetechnique.trainingview.ui.programs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import ru.fm4m.exercisetechnique.trainingdomain.data.UserTraining
import ru.fm4m.exercisetechnique.trainingview.databinding.FragmentAvailableProgramsBinding
import javax.inject.Inject

class AvailableProgramsFragment : Fragment() {

    private lateinit var viewModel: AvailableProgramsViewModel

    @Inject
    lateinit var factoryViewModel: AvailableProgramsViewModel.Companion.Factory

    companion object {
        fun newInstance(): AvailableProgramsFragment {
            return AvailableProgramsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate $this")
        AndroidSupportInjection.inject(this)

        viewModel =
            ViewModelProvider(this, factoryViewModel)[AvailableProgramsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val viewBinding = FragmentAvailableProgramsBinding.inflate(inflater, container, false)
        viewBinding.adapter = AvailableProgramsAdapted(object : AvailableProgramsAdapted.ProgramClickListener {

            override fun onProgramClicked(userTraining: UserTraining?) {
                if (userTraining != null) {
                    viewModel.programClickListener.onProgramClicked(userTraining.id)
                }
            }

        })

        viewBinding.refreshLayout.setOnRefreshListener {
            viewModel.restoreAllUserTraining()
        }


        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d("TAG", "repeatOnLifecycle(Lifecycle.State.STARTED)")
                var lastTimeDownload = false
                viewModel.uiState.collect { state ->
                    Log.d("TAG", "getNewState $state")
                    if (state.userTrainings.isEmpty() && !state.loadingTraining && state.throwable == null) {
                        if (!lastTimeDownload) {
                            lastTimeDownload = true
                            viewModel.restoreAllUserTraining()
                        }
                    }

                    if (state.userTrainings != viewBinding.adapter?.currentList) {
                        lastTimeDownload = false
                        viewBinding.adapter?.submitList(state.userTrainings)
                    }

                    if (state.throwable != null) {
                        //show error
                        lastTimeDownload = false
                    }

                    if (!state.loadingTraining) {
                        viewBinding.refreshLayout.isRefreshing = false
                    }

                }
            }
        }

        return viewBinding.root
    }
}