package ru.fm4m.exercisetechnique.techview.newprogram

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_new_program.view.*
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techview.core.LoadableRecyclerAdapter
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.videosearch.ShortInfoListListener
import ru.fm4m.exercisetechnique.videosearch.ShortInfoVideosListAdapter
import javax.inject.Inject

class NewProgramFragment : Fragment(),
    LoadableRecyclerAdapter.ErrorListener,
    ShortInfoListListener,
    Consumer<NewProgramFeature.State>,
    ObservableSource<UIEventNewProgram> {


    private lateinit var adapter: ShortInfoVideosListAdapter

    val newsConsumer =
        Consumer<NewProgramFeature.News>{ t ->
            if (t is NewProgramFeature.News.ErrorExecuteRequest) {
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

    private var savedInstanceState: Bundle? = null

    @Inject
    lateinit var binding: NewProgramBinding
    @Inject
    lateinit var uiEventSource : PublishSubject<UIEventNewProgram>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        AndroidSupportInjection.inject(this)
        binding.setup(this)
    }

    fun getLastSavedState(): Bundle? {
        return savedInstanceState
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_new_program, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ShortInfoVideosListAdapter(this, this)
        view.recyclerVideosInfo.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        uiEventSource.onNext(UIEventNewProgram.RedownloadAllVideos)
    }

    override fun onRetryClicked() {
        if (adapter.getNestedItemCount() == 0) {
            uiEventSource.onNext(UIEventNewProgram.RedownloadAllVideos)
        }
    }

    override fun onClickVideoInfo(videoInfo: VideoInfo) {
        uiEventSource.onNext(UIEventNewProgram.ShowVideo(videoInfo))
    }

    override fun accept(t: NewProgramFeature.State) {
        if (::adapter.isInitialized) {
            adapter.setVideos(t.videoLists)
            adapter.showLoading(t.isLoading)
        }
    }

    override fun subscribe(observer: Observer<in UIEventNewProgram>) {
        uiEventSource.subscribe(observer)
    }
}