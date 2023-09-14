package ru.fm4m.exercisetechnique.techview.videolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_video_list.toolbar
import kotlinx.android.synthetic.main.fragment_video_list.view.*
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techview.core.LoadableRecyclerAdapter
import ru.fm4m.exercisetechnique.techview.core.NavigationEvent
import ru.fm4m.exercisetechnique.techview.core.NavigationPublisherContainer
import ru.fm4m.exercisetechnique.techview.core.findNavigationPublisher
import ru.fm4m.exercisetechnique.techview.onevideo.ExerciseInfoFragment
import javax.inject.Inject


class VideoListFragment : Fragment(R.layout.fragment_video_list),
    Consumer<VideoListFeature.State>,
    ObservableSource<UIEventVideos>,
    LoadableRecyclerAdapter.ErrorListener {

    private var adapter: VideoListAdapter? = null
    private var recyclerView: RecyclerView? = null

    @Inject
    lateinit var bindings: VideoListBindings

    @Inject
    lateinit var uiEventSource: PublishSubject<UIEventVideos>

    val newsConsumer =
        Consumer<VideoListFeature.News> { t ->
            if (t is VideoListFeature.News.ErrorExecuteRequest) {
                Log.d("TAG", "newsConsumer react")
                var message = t.throwable.message
                if (message.isNullOrBlank()) {
                    message = t.throwable.toString()
                }
                adapter?.showError(message)
            }
        }


    private var lastSavedState: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lastSavedState = savedInstanceState
        AndroidSupportInjection.inject(this)

        bindings.setup(this)
    }

    fun getSex(): Sex {
        return requireArguments().getSerializable(ARG_SEX) as Sex
    }

    fun getMuscle() = requireArguments().getSerializable(ARG_MUSCLE) as Muscle

    fun getMuscleName() = requireArguments().getString(ARG_MUSCLE_NAME, "")

    fun getSavedState() = lastSavedState

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = VideoListAdapter(this)
        recyclerView = view.recyclerVideos
        recyclerView?.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        recyclerView = null
    }

    override fun onResume() {
        super.onResume()
        uiEventSource.onNext(UIEventVideos.RedownloadAllVideos)
    }

    override fun subscribe(observer: Observer<in UIEventVideos>) {
        uiEventSource.subscribe(observer)
    }

    override fun accept(t: VideoListFeature.State) {
        Log.d("TAG", "new state loading ${t.isLoading} items ${t.videoLists}")

        toolbar.title = t.muscleName
        val adapter = adapter
        if (adapter != null) {
            adapter.setItems(t.videoLists)
            adapter.showLoading(t.isLoading)
        }
    }

    // TODO: 10/9/21 implement error News

    override fun onRetryClicked() {
        // TODO: 10/9/21 implement this
        if (adapter?.getNestedItemCount() == 0) {
            uiEventSource.onNext(UIEventVideos.RedownloadAllVideos)
//        } else {

        }
    }

    companion object {

        const val ARG_SEX = "sex"
        const val ARG_MUSCLE = "muscle"
        const val ARG_MUSCLE_NAME = "muscle_name"

        @JvmStatic
        fun newInstance(sex: Sex, muscle: Muscle, muscleName: String) =
            VideoListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SEX, sex)
                    putSerializable(ARG_MUSCLE, muscle)
                    putString(ARG_MUSCLE_NAME, muscleName)
                }
            }
    }
}