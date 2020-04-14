package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter: RecyclerView.Adapter<SleepNightAdapter.NightViewHolder>() {
    var data = listOf<SleepNight>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NightViewHolder {
        return NightViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NightViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


    class NightViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        //could use Kotlin Synthetics
        val sleepLength = itemView.findViewById<TextView>(R.id.tv_sleep_length)
        val sleepQuality = itemView.findViewById<TextView>(R.id.tv_sleep_qualtiy)
        val sleepQualityImage = itemView.findViewById<ImageView>(R.id.iv_sleep_quality)

        fun bind(item: SleepNight ) {
            val res = itemView.context.resources
            sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            sleepQuality.text = convertNumericQualityToString(item.sleepQuality, res)
            sleepQualityImage.setImageResource(when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })

        }

        companion object {
            fun from(parent: ViewGroup): NightViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_sleep_night, parent, false)
                return NightViewHolder(view)
            }
        }
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>(){
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }
}