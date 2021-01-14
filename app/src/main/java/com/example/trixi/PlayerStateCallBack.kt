package com.example.trixi

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.Player

interface PlayerStateCallback {

    fun onVideoDurationRetrieved(duration: Long, player: Player)

    fun onVideoBuffering(player: Player)

    fun onStartedPlaying(player: Player)

    fun onFinishedPlaying(player: Player)
}

internal class VideoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), PlayerStateCallback {

    val mList = mutableListOf<String>()

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemVideoBinding>(
            LayoutInflater.from(parent.context), R.layout.item_video, parent, false
        )
        return VideoViewHolder(binding)
    }

    fun onBindViewHolder(viewHolder: VideoViewHolder, position: Int) {
        val videoUrl = mList[position]
        with(viewHolder.binding) {
            url = videoUrl
            callback = this@VideoAdapter
            executePendingBindings()
        }
    }


    class VideoViewHolder: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)
}