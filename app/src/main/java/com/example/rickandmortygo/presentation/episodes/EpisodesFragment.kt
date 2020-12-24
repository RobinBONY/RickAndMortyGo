package com.example.rickandmortygo.presentation.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortygo.R
import com.example.rickandmortygo.repository.RetrofitController
import kotlinx.android.synthetic.main.fragment_episodes.*

class EpisodesFragment : Fragment() {

    lateinit var adapter: EpisodesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_episodes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_episodes_list.layoutManager = GridLayoutManager(activity,1)
        adapter = EpisodesAdapter()
        rv_episodes_list.adapter = adapter
        rv_episodes_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    RetrofitController.fetchNextEpisodesPage()
                }
            }
        })
        RetrofitController.error.observe(this, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        fetchEpisodes()
    }

    private fun fetchEpisodes() {

        RetrofitController.episodesList.observe(this, Observer {
                episodes ->
            adapter?.setData(episodes)
        })

        RetrofitController.fetchAllEpisodes()
    }
}