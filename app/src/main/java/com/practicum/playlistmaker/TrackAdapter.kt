package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackAdapter (val trackList: MutableList<Track>): RecyclerView.Adapter<TrackViewHolder>() {

    private var filteredTrackList: MutableList<Track> = trackList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = filteredTrackList[position]
        holder.trackName.text = track.trackName
        holder.artistName.text = track.artistName
        holder.trackTime.text = track.trackTime
        Glide.with(holder.trackImage.context).load(track.artworkUrl100).into(holder.trackImage)
    }

    override fun getItemCount(): Int {
        return filteredTrackList.size
    }

    fun filterList(query: String) {
        filteredTrackList = if (query.isEmpty()) {
            trackList.toMutableList()
        } else {
            trackList.filter { track ->
                track.trackName.contains(query, ignoreCase = true) ||
                        track.artistName.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}