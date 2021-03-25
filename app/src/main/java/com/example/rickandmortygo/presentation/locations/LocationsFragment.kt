package com.example.rickandmortygo.presentation.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortygo.R
import com.example.rickandmortygo.presentation.characters.CharactersViewModel
import kotlinx.android.synthetic.main.fragment_locations.*

class LocationsFragment : Fragment() {

    lateinit var locationsViewModel: LocationsViewModel
    lateinit var adapter: LocationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_locations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationsViewModel = ViewModelProviders.of(activity!!).get(LocationsViewModel::class.java)
        locationsViewModel.clear()

        rv_locations_list.layoutManager = GridLayoutManager(activity,1)

        adapter = LocationsAdapter()
        rv_locations_list.adapter = adapter

        rv_locations_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    locationsViewModel.fetchNextLocationsPage()
                }
            }
        })
        locationsViewModel.observeError(this)
        locationsViewModel.error.observe(this, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        fetchLocations()
    }

    private fun fetchLocations() {

        locationsViewModel.observeLocations(this)
        locationsViewModel.locationsList.observe(this, Observer {
                locations ->
            adapter?.setData(locations)
        })

        locationsViewModel.fetchAllLocations()
    }
}