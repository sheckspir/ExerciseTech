package ru.fm4m.exercisetechnique.techview.videosearch

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_video_search.view.*
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techview.core.LoadableRecyclerAdapter
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techview.core.NavigationEvent
import ru.fm4m.exercisetechnique.techview.core.NavigationPublisherContainer
import ru.fm4m.exercisetechnique.techview.core.ShortInfoListListener
import ru.fm4m.exercisetechnique.techview.core.ShortInfoVideosListAdapter
import ru.fm4m.exercisetechnique.techview.core.findNavigationPublisher
import ru.fm4m.exercisetechnique.techview.onevideo.ExerciseInfoFragment
import ru.fm4m.exercisetechnique.techview.videolist.VideoItemVH
import javax.inject.Inject

class SearchVideosFragment : Fragment(R.layout.fragment_video_search),
    LoadableRecyclerAdapter.ErrorListener,
    ShortInfoListListener,
    Consumer<SearchVideosFeature.State>,
    ObservableSource<UIEventSearchVideos>,
    NavigationPublisherContainer {

    @SuppressLint("CheckResult")
    override val navigationPublisher = PublishSubject.create<NavigationEvent>().apply {
        this.subscribe { event ->
            if (event is NavigationEvent.ShowOneVideo) {
                val position = adapter?.getPosition(event.videoInfo.id)
                val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position?: -1)
                val extras = if (viewHolder is VideoItemVH) {
                    FragmentNavigatorExtras(
                        viewHolder.textView to "title_${event.videoInfo.id}"
                    )
                } else {
                    null
                }

                findNavController().navigate(
                    resId = R.id.action_searchVideosFragment_to_oneVideoFragment,
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
    private var recyclerView : RecyclerView? = null

    val newsConsumer =
        Consumer<SearchVideosFeature.News> { t ->
            if (t is SearchVideosFeature.News.ErrorExecuteRequest) {
                Log.d("TAG","newsConsumer react")
                var message = t.throwable.message
                if (message.isNullOrBlank()) {
                    message = t.throwable.toString()
                }
                adapter?.showError(message)
            }
        }

    private var lastState : Bundle? = null

    @Inject
    lateinit var bindings: SearchVideoBindings
    @Inject
    lateinit var uiEventSource : PublishSubject<UIEventSearchVideos>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lastState = savedInstanceState

        AndroidSupportInjection.inject(this)

        bindings.setup(this)
    }

    fun getLastState() : Bundle? {
        return lastState
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ShortInfoVideosListAdapter(this, this)
        recyclerView = view.recyclerSearchResult
        recyclerView?.adapter = adapter
        view.editTextSearch.addTextChangedListener {
            uiEventSource.onNext(UIEventSearchVideos.SearchVideos(it?.trim().toString()))
        }

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
        uiEventSource.onNext(UIEventSearchVideos.RedownloadAllVideos)
    }

    override fun onClickVideoInfo(videoInfo: VideoInfo) {
        uiEventSource.onNext(UIEventSearchVideos.ShowVideo(videoInfo))
    }

    override fun onRetryClicked() {
        if (adapter?.getNestedItemCount() == 0) {
            uiEventSource.onNext(UIEventSearchVideos.RedownloadAllVideos)

        }
    }

    override fun accept(t: SearchVideosFeature.State) {
        adapter?.setVideos(t.videoLists)
        adapter?.showLoading(t.isLoading)
    }

    override fun subscribe(observer: Observer<in UIEventSearchVideos>) {
        uiEventSource.subscribe(observer)
    }
}