package ru.fm4m.exercisetechnique.videolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badoo.mvicore.android.AndroidTimeCapsule
import dagger.android.support.AndroidSupportInjection
import ru.fm4m.exercisetechnique.R
import ru.fm4m.exercisetechnique.core.LoadableRecyclerAdapter
import ru.fm4m.exercisetechnique.model.Muscle
import ru.fm4m.exercisetechnique.model.Sex
import ru.fm4m.exercisetechnique.server.ServerApiImpl
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_video_list.view.*
import ru.fm4m.exercisetechnique.ExerciseApplication
import javax.inject.Inject


class VideoListFragment : Fragment(), Consumer<VideoListFeature.State>, ObservableSource<UIEventVideos>, LoadableRecyclerAdapter.ErrorListener {


    private lateinit var adapter: VideoListAdapter

    @Inject
    lateinit var bindings: VideoListBindings
    @Inject
    lateinit var uiEventSource : PublishSubject<UIEventVideos>

    val newsConsumer =
        Consumer<VideoListFeature.News> { t ->
            if (t is VideoListFeature.News.ErrorExecuteRequest) {
                Log.d("TAG","newsConsumer react")
                if (::adapter.isInitialized) {
                    var message = t.throwable.message
                    if (message.isNullOrBlank()) {
                        message = t.throwable.toString()
                    }
                    adapter.showError(message)
                }
            }
        }


    private var lastSavedState : Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lastSavedState = savedInstanceState
        AndroidSupportInjection.inject(this)

        bindings.setup(this)
    }

    fun getSex() : Sex {
        return requireArguments().getSerializable(ARG_SEX) as Sex
    }

    fun getMuscle() = requireArguments().getSerializable(ARG_MUSCLE) as Muscle

    fun getSavedState() = lastSavedState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = VideoListAdapter(this)
        view.recyclerVideos.adapter = adapter
        adapter.notifyDataSetChanged()
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
        if (::adapter.isInitialized) {
            adapter.setMuscle(t.muscle)
            adapter.setItems(t.videoLists)
            adapter.showLoading(t.isLoading)
        }
    }

    // TODO: 10/9/21 implement error News

    override fun onRetryClicked() {
        // TODO: 10/9/21 implement this
        if (adapter.getNestedItemCount() == 0) {
            uiEventSource.onNext(UIEventVideos.RedownloadAllVideos)
//        } else {

        }
    }

    companion object {

        const val ARG_SEX = "sex"
        const val ARG_MUSCLE = "muscle"

        @JvmStatic
        fun newInstance(sex: Sex, muscle: Muscle) =
            VideoListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SEX, sex)
                    putSerializable(ARG_MUSCLE, muscle)
                }
            }
    }
}