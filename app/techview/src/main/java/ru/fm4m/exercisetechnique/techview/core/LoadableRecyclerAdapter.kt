package ru.fm4m.exercisetechnique.techview.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fm4m.exercisetechnique.techview.R
import kotlinx.android.synthetic.main.item_error.view.*

abstract class LoadableRecyclerAdapter<T, VH : RecyclerView.ViewHolder>(
    private val errorListener: ErrorListener
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var loading: Boolean = false
    private var errorMessage : String? = null

    fun showLoading(loading: Boolean) {
        this.loading = loading
        errorMessage = null
        notifyDataSetChanged()
    }

    fun showError(message: String) {
        errorMessage = message
        notifyDataSetChanged()
    }

    abstract fun getItem(position: Int) : T

    abstract fun getNestedItemCount() : Int

    abstract fun onCreateNestedViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindNestedViewHolder(holder: VH, position: Int)

    override fun getItemViewType(position: Int): Int {
        if (position == getNestedItemCount()) {
            return if (loading) {
                TYPE_LOADING
            } else {
                TYPE_ERROR
            }
        }
        return super.getItemViewType(position)
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_LOADING -> object : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false)){}
            TYPE_ERROR -> ErrorVH(LayoutInflater.from(parent.context).inflate(R.layout.item_error, parent, false),errorListener)
            else -> {
                onCreateNestedViewHolder(parent, viewType)
            }
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == TYPE_ERROR) {
            (holder as ErrorVH).bind(errorMessage?:"")
        } else if (viewType != TYPE_LOADING) {
            @Suppress("UNCHECKED_CAST")
            onBindNestedViewHolder(holder as VH, position)
        }
    }

    final override fun getItemCount(): Int {
        return if (loading || errorMessage != null) {
            getNestedItemCount() + 1
        } else {
            getNestedItemCount()
        }
    }

    companion object {
        private const val TYPE_LOADING = 8;
        private const val TYPE_ERROR = 9;
    }

    interface ErrorListener {
        fun onRetryClicked()
    }
}

private class ErrorVH(view: View, private val listener: LoadableRecyclerAdapter.ErrorListener) : RecyclerView.ViewHolder(view) {

    private val errorTextView = view.textError
    init {
        view.buttonRetry.setOnClickListener {
            listener.onRetryClicked()
        }
    }

    fun bind(message: String) {
        errorTextView.text = message
    }
}

