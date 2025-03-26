package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackAdapter (val trackList: MutableList<Track>): RecyclerView.Adapter<TrackViewHolder>() {

    private var filteredTrackList: List<Track> = trackList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = filteredTrackList[position]
        holder.bind(track)

    }

    override fun getItemCount(): Int {
        return filteredTrackList.size
    }
}