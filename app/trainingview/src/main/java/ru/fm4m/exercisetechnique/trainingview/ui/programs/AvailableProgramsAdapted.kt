package ru.fm4m.exercisetechnique.trainingview.ui.programs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.fm4m.exercisetechnique.trainingdomain.data.UserTraining
import ru.fm4m.exercisetechnique.trainingview.databinding.ItemProgramBinding

class AvailableProgramsAdapted(
    private val clickListener: ProgramClickListener,
) : ListAdapter<UserTraining, AvailableProgramsAdapted.ProgramVH>(DiffUtilPrograms()) {

    interface ProgramClickListener {
        fun onProgramClicked(userTraining: UserTraining?)
    }

    inner class ProgramVH(
        private val binding: ItemProgramBinding,
        clickListener: ProgramClickListener,
    ) : ViewHolder(binding.root) {

        init {
            binding.clickListener = clickListener
        }

        fun bindItem(training: UserTraining) {
            binding.training = training
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramVH {
        return ProgramVH(
            ItemProgramBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: ProgramVH, position: Int) {
        holder.bindItem(getItem(position))
    }


}

private class DiffUtilPrograms : DiffUtil.ItemCallback<UserTraining>() {

    override fun areItemsTheSame(oldItem: UserTraining, newItem: UserTraining): Boolean {
        return oldItem.id == newItem.id

    }

    override fun areContentsTheSame(oldItem: UserTraining, newItem: UserTraining): Boolean {
        return oldItem == newItem
    }
}