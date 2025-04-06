package com.practicum.playlistmaker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackAdapter (private var trackList: List<Track>, private val onTrackClick: (Track) -> Unit): RecyclerView.Adapter<TrackViewHolder>() {

    //private var filteredTrackList: List<Track> = trackList

    fun updateTrackList(newTrackList: List<Track>) {
        trackList = newTrackList.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            Log.d("TrackAdapter", "Track clicked: ${track.trackName}")
            onTrackClick(track)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

}