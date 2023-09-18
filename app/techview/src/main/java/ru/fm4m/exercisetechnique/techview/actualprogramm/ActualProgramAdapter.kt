package ru.fm4m.exercisetechnique.techview.actualprogramm

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.fm4m.exercisetechnique.techdomain.data.ProgramInfo
import ru.fm4m.exercisetechnique.techdomain.data.ProgramPartShort
import ru.fm4m.exercisetechnique.techview.R

class ActualProgramAdapter(private val onDayClicked: (Int) -> Unit) : Adapter<ViewHolder>() {

    private var lastProgramm: ProgramInfo? = null

    private var descriptionList = emptyList<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateProgramm(program: ProgramInfo) {
        if (lastProgramm != program) {
            val daysEqual = lastProgramm?.days == program.days
            val lastCountDays = lastProgramm?.days?.size
            val descriptionEqual = lastProgramm?.description == program.description
            lastProgramm = program
            updateCount()
            if (descriptionEqual) {
                notifyDataSetChanged()
            } else if (!daysEqual && lastCountDays != null) {
                if (lastCountDays == program.days.size) {
                    notifyItemRangeChanged(0, program.days.size)
                } else if (lastCountDays > program.days.size) {
                    notifyItemRangeChanged(0, program.days.size)
                    notifyItemRangeRemoved(program.days.size - 1, lastCountDays - program.days.size)
                } else {
                    notifyItemRangeChanged(0, lastCountDays)
                    notifyItemRangeInserted(lastCountDays - 1, program.days.size - lastCountDays)
                }
            } else {
                //это не должно произойти, но на всякий случай
                notifyDataSetChanged()
            }

        }

    }

    private fun updateCount() {
        descriptionList = lastProgramm?.description?.split("\n\n") ?: emptyList()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < (lastProgramm?.days?.size ?: 0)) {
            TYPE_DAY
        } else {
            TYPE_DESCR
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_DAY -> DayViewHolder(
                onDayClicked,
                LayoutInflater.from(parent.context).inflate(R.layout.item_day_short, parent, false)
            )

            TYPE_DESCR -> DescriptionPartVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_description_part, parent, false)
            )

            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_DAY -> (holder as DayViewHolder).bind(lastProgramm!!.days[position])
            TYPE_DESCR -> (holder as DescriptionPartVH).bind(descriptionList[position - lastProgramm!!.days.size])
        }
    }

    override fun getItemCount(): Int {
        return (lastProgramm?.days?.size?:0) + descriptionList.size
    }

    companion object {
        private const val TYPE_DAY = 10
        private const val TYPE_DESCR = 20
    }
}

class DayViewHolder(val onDayClicked: (Int) -> Unit, view: View) : ViewHolder(view) {

    val imageView = view.findViewById<ImageView>(R.id.dayImageView)
    val textView = view.findViewById<TextView>(R.id.dayNameTextView)
    private val countExerciseTextView = view.findViewById<TextView>(R.id.exercisesCountTextView)

    private var dayId: Int? = null

    init {
        view.setOnClickListener {
            val dayId = dayId
            if (dayId != null) {
                onDayClicked.invoke(dayId)
            }
        }
    }

    fun bind(day: ProgramPartShort) {
        this.dayId = day.id
        //todo изображение для дня
        textView.text = textView.resources.getString(R.string.day_title, day.name)
        countExerciseTextView.text = countExerciseTextView.resources.getQuantityString(
            R.plurals.plural_exercises,
            day.exerciseCount,
            day.exerciseCount
        )
    }

}

class DescriptionPartVH(view: View) : ViewHolder(view) {

    val textView = view.findViewById<TextView>(R.id.textView)

    fun bind(string: String) {
        textView.text = string
    }
}