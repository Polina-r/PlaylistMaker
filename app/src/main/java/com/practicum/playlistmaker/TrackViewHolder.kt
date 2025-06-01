package com.practicum.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TrackViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val trackName: TextView = itemView.findViewById(R.id.trackName)
    val artistName: TextView = itemView.findViewById(R.id.artistName)
    val trackTimeMillis: TextView = itemView.findViewById(R.id.trackTime)
    val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    val iconBetweenArtistAndTime: ImageView = itemView.findViewById(R.id.iconBetweenArtistAndTime)


    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val context = itemView.context
        val cornerRadiusPx = dpToPx(2f, context)

        Glide.with(context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(cornerRadiusPx))
            .into(trackImage)
    }


    //Функция перевода dp в px
    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}