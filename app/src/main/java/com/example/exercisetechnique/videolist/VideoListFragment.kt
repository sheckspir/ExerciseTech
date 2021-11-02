package com.example.exercisetechnique.videolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.example.exercisetechnique.R
import com.example.exercisetechnique.core.LoadableRecyclerAdapter
import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.Sex
import com.example.exercisetechnique.server.ServerApiImpl
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_video_list.view.*


class VideoListFragment : Fragment(), Consumer<VideoListFeature.State>, ObservableSource<UIEventVideos>, LoadableRecyclerAdapter.ErrorListener {

    private lateinit var bindings: VideoListBindings
    private lateinit var adapter: VideoListAdapter
    private val uiEventSource = PublishSubject.create<UIEventVideos>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sex = requireArguments().getSerializable(ARG_SEX) as Sex
        val muscle = requireArguments().getSerializable(ARG_MUSCLE) as Muscle
        val api = ServerApiImpl.getInstance()
        val feature = VideoListFeature(AndroidTimeCapsule(savedInstanceState), api, sex, muscle)
        bindings = VideoListBindings(this, feature)
        bindings.setup(this)

    }

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