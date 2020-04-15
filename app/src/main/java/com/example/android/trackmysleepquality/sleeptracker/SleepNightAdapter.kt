package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ItemSleepNightBinding

class SleepNightAdapter: ListAdapter<SleepNight, SleepNightAdapter.NightViewHolder>(SleepNightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NightViewHolder {
        return NightViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NightViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class NightViewHolder private constructor(val binding: ItemSleepNightBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: SleepNight ) {
            binding.sleepNight = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): NightViewHolder {
                val binding = ItemSleepNightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return NightViewHolder(binding)
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