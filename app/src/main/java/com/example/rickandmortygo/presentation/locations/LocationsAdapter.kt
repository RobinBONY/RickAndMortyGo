package com.example.rickandmortygo.presentation.locations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortygo.R
import com.example.rickandmortygo.data.Location
import kotlinx.android.synthetic.main.adapter_episode.view.*

class LocationsAdapter :
    RecyclerView.Adapter<LocationsAdapter.LocationsViewHolder>() {

    private var locationsList = ArrayList<Location>()

    inner class LocationsViewHolder(private val locationsView: View) :
        RecyclerView.ViewHolder(locationsView) {

        fun bind(location: Location) {
            locationsView.episodeName.text = location.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_location, parent, false)
        return LocationsViewHolder(v)
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        holder.bind(locationsList[position])
    }

    override fun getItemCount(): Int {
        return locationsList.size
    }

    fun setData(locations: List<Location>?) {
        if (locations == null) {
            return
        }
        this.locationsList.addAll(locations)
        notifyDataSetChanged()
    }
}