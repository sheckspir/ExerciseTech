package ru.fm4m.exercisetechnique.techview.actualprogramm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewStub
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.android.support.AndroidSupportInjection
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_program.view.progressMain
import kotlinx.android.synthetic.main.fragment_program.view.recyclerInfo
import kotlinx.android.synthetic.main.fragment_program.view.refreshLayout
import kotlinx.android.synthetic.main.fragment_program.view.toolbar
import ru.fm4m.exercisetechnique.techdomain.data.ProgramInfo
import ru.fm4m.exercisetechnique.techview.R
import javax.inject.Inject

class ActualProgramFragment : Fragment(R.layout.fragment_program),
    ObservableSource<UIEventActualProgram>,
    Consumer<ActualProgramFeature.State> {

    private var adapter: ActualProgramAdapter? = null
    private var toolbarTitle : Toolbar? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var progressBar : ProgressBar? = null
    private var errorView : View? = null
    private var errorTextView : TextView? = null

    private var savedInstanceState: Bundle? = null

    @Inject
    lateinit var binding: ActualProgramBinding

    @Inject
    lateinit var uiEventSource: PublishSubject<UIEventActualProgram>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        AndroidSupportInjection.inject(this)
        binding.setup(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ActualProgramAdapter {
            uiEventSource.onNext(UIEventActualProgram.ShowDay(it))
        }
        view.recyclerInfo.adapter = adapter
        toolbarTitle = view.toolbar
        swipeRefreshLayout = view.refreshLayout
        progressBar = view.progressMain

        swipeRefreshLayout?.setOnRefreshListener {
            downloadProgram()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        toolbarTitle = null
        swipeRefreshLayout = null
        progressBar = null
        errorView = null
        errorTextView = null
    }

    override fun onResume() {
        super.onResume()
        downloadProgram()
    }

    private fun downloadProgram() {
        uiEventSource.onNext(UIEventActualProgram.DownloadProgram)
    }

    private fun onUpdateProgram(program: ProgramInfo) {
        toolbarTitle?.title = program.name
        adapter?.updateProgramm(program)
    }

    fun getLastSavedState(): Bundle? {
        return savedInstanceState
    }

    override fun subscribe(observer: Observer<in UIEventActualProgram>) {
        Log.d("TAG", "subscribe uiEventSource")
        uiEventSource.subscribe(observer)
    }

    override fun accept(t: ActualProgramFeature.State) {
        if (t.loading) {
            if (t.program == null) {
                progressBar?.visibility = View.VISIBLE
            } else {
                if (swipeRefreshLayout?.isRefreshing != true) {
                    swipeRefreshLayout?.isRefreshing = true
                }
            }
        } else {
            progressBar?.visibility = View.GONE
            swipeRefreshLayout?.isRefreshing = false
        }
        if (t.error != null) {
            if (errorView == null) {
                errorView = view?.findViewById<ViewStub>(R.id.stubError)?.inflate()
                errorTextView = errorView?.findViewById(R.id.errorTextView)
                errorView?.findViewById<Button>(R.id.buttonRetry)?.setOnClickListener {
                    downloadProgram()
                }

            }
            errorView?.visibility = View.VISIBLE
            errorTextView?.text = t.error.message
        } else {
            errorView?.visibility = View.GONE
        }
        if (t.program != null) {
            onUpdateProgram(t.program)
        }
    }
}
