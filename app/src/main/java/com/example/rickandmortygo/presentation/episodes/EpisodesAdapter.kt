package com.example.rickandmortygo.presentation.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortygo.R
import com.example.rickandmortygo.data.Episode
import kotlinx.android.synthetic.main.adapter_episode.view.*

class EpisodesAdapter :
    RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder>() {

    private var episodesList = ArrayList<Episode>()

    inner class EpisodesViewHolder(private val episodesView: View) :
        RecyclerView.ViewHolder(episodesView) {

        fun bind(episode: Episode) {
            episodesView.episodeNumber.text = episode.episode
            episodesView.episodeName.text = episode.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_episode, parent, false)
        return EpisodesViewHolder(v)
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        holder.bind(episodesList[position])
    }

    override fun getItemCount(): Int {
        return episodesList.size
    }

    fun setData(episodes: List<Episode>?) {
        if (episodes == null) {
            return
        }
        this.episodesList.addAll(episodes)
        notifyDataSetChanged()
    }
}