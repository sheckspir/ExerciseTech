package ru.fm4m.exercisetechnique.techview.videosearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_video_search.view.*
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techview.core.LoadableRecyclerAdapter
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.videosearch.ShortInfoListListener
import ru.fm4m.exercisetechnique.videosearch.ShortInfoVideosListAdapter
import javax.inject.Inject

class SearchVideosFragment : Fragment(),
    LoadableRecyclerAdapter.ErrorListener,
    ShortInfoListListener,
    Consumer<SearchVideosFeature.State>,
    ObservableSource<UIEventSearchVideos> {

    private lateinit var adapter: ShortInfoVideosListAdapter

    val newsConsumer =
        Consumer<SearchVideosFeature.News> { t ->
            if (t is SearchVideosFeature.News.ErrorExecuteRequest) {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_video_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ShortInfoVideosListAdapter(this, this)
        view.recyclerSearchResult.adapter = adapter
        view.editTextSearch.addTextChangedListener {
            uiEventSource.onNext(UIEventSearchVideos.SearchVideos(it?.trim().toString()))
        }
    }

    override fun onResume() {
        super.onResume()
        uiEventSource.onNext(UIEventSearchVideos.RedownloadAllVideos)
    }

    override fun onClickVideoInfo(videoInfo: VideoInfo) {
        uiEventSource.onNext(UIEventSearchVideos.ShowVideo(videoInfo))
    }

    override fun onRetryClicked() {
        if (adapter.getNestedItemCount() == 0) {
            uiEventSource.onNext(UIEventSearchVideos.RedownloadAllVideos)

        }
    }

    override fun accept(t: SearchVideosFeature.State) {
        if (::adapter.isInitialized) {
            adapter.setVideos(t.videoLists)
            adapter.showLoading(t.isLoading)
        }
    }

    override fun subscribe(observer: Observer<in UIEventSearchVideos>) {
        uiEventSource.subscribe(observer)
    }
}