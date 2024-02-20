package com.practicum.playlistmaker.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.playlist.dto.DiffCallback
import com.practicum.playlistmaker.domain.search.models.PlayList

class PlayListsAdapter(private val clickListener: PlayListClickListener) :
    RecyclerView.Adapter<PlayListViewHolder>() {

    var playLists = listOf<PlayList>()
        set(newList) {
            val diffResult = DiffUtil.calculateDiff(
                object : DiffCallback<PlayList>(field, newList) {
                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return field[oldItemPosition].playListId == newList[newItemPosition].playListId
                    }
                }
            )
            field = newList
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        return PlayListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        )
    }

    override fun getItemCount(): Int = playLists.size

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playLists[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(playLists[holder.adapterPosition])
        }
    }

    fun interface PlayListClickListener {
        fun onClick(playList: PlayList)
    }
}