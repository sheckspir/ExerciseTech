package ru.fm4m.exercisetechnique.techview.newprogram

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
import kotlinx.android.synthetic.main.fragment_new_program.view.*
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techview.core.LoadableRecyclerAdapter
import ru.fm4m.exercisetechnique.techview.core.NavigationEvent
import ru.fm4m.exercisetechnique.techview.core.NavigationPublisherContainer
import ru.fm4m.exercisetechnique.techview.core.findNavigationPublisher
import ru.fm4m.exercisetechnique.techview.onevideo.ExerciseInfoFragment
import ru.fm4m.exercisetechnique.techview.videolist.VideoItemVH
import ru.fm4m.exercisetechnique.techview.core.ShortInfoListListener
import ru.fm4m.exercisetechnique.techview.core.ShortInfoVideosListAdapter
import javax.inject.Inject

class NewProgramFragment : Fragment(R.layout.fragment_new_program),
    LoadableRecyclerAdapter.ErrorListener,
    ShortInfoListListener,
    Consumer<NewProgramFeature.State>,
    ObservableSource<UIEventNewProgram>,
    NavigationPublisherContainer {

    @SuppressLint("CheckResult")
    override val navigationPublisher = PublishSubject.create<NavigationEvent>().apply {
        this.subscribe { event ->
            if (event is NavigationEvent.ShowOneVideoFromNewProgram) {
                val viewHolder = recyclerView?.findViewHolderForAdapterPosition(
                    adapter?.getPosition(event.videoInfo.id) ?: -1
                )
                val extras = if (viewHolder is VideoItemVH) {
                    FragmentNavigatorExtras(
                        viewHolder.textView to "title_${event.videoInfo.id}"
                    )
                } else {
                    null
                }
                findNavController().navigate(
                    resId = R.id.action_newProgramFragment_to_oneVideoFragment,
                    args = Bundle().apply {
                        this.putSerializable(ExerciseInfoFragment.ARG_VIDEO_INFO, event.videoInfo)
                    },
                    navOptions = null,
                    navigatorExtras = extras
                )
            } else {
                //send to parent
                parentFragment?.findNavigationPublisher()?.onNext(event)
            }
        }
    }


    private var adapter: ShortInfoVideosListAdapter? = null
    private var recyclerView: RecyclerView? = null

    val newsConsumer =
        Consumer<NewProgramFeature.News> { t ->
            if (t is NewProgramFeature.News.ErrorExecuteRequest) {
                Log.d("TAG", "newsConsumer react")
                var message = t.throwable.message
                if (message.isNullOrBlank()) {
                    message = t.throwable.toString()
                }
                adapter?.showError(message)
            }
        }

    private var savedInstanceState: Bundle? = null

    @Inject
    lateinit var binding: NewProgramBinding

    @Inject
    lateinit var uiEventSource: PublishSubject<UIEventNewProgram>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        AndroidSupportInjection.inject(this)
        binding.setup(this)
    }

    fun getLastSavedState(): Bundle? {
        return savedInstanceState
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ShortInfoVideosListAdapter(this, this)
        recyclerView = view.recyclerVideosInfo

        recyclerView?.adapter = adapter

        postponeEnterTransition()
        recyclerView?.viewTreeObserver?.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        recyclerView = null
    }

    override fun onResume() {
        super.onResume()
        uiEventSource.onNext(UIEventNewProgram.RedownloadAllVideos)
    }

    override fun onRetryClicked() {
        if (adapter?.getNestedItemCount() == 0) {
            uiEventSource.onNext(UIEventNewProgram.RedownloadAllVideos)
        }
    }

    override fun onClickVideoInfo(videoInfo: VideoInfo) {
        uiEventSource.onNext(UIEventNewProgram.ShowVideo(videoInfo))
    }

    override fun accept(t: NewProgramFeature.State) {
        adapter?.setVideos(t.videoLists)
        adapter?.showLoading(t.isLoading)
    }

    override fun subscribe(observer: Observer<in UIEventNewProgram>) {
        uiEventSource.subscribe(observer)
    }
}