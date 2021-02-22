package com.example.rickandmortygo.presentation.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortygo.R
import com.example.rickandmortygo.repository.RepositoryController
import kotlinx.android.synthetic.main.fragment_characters.*

class CharactersFragment : Fragment() {

    lateinit var adapter: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_characters_list.layoutManager = GridLayoutManager(activity,3)
        adapter = CharactersAdapter()
        rv_characters_list.adapter = adapter
        rv_characters_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    RepositoryController.fetchNextCharactersPage()
                }
            }
        })
        RepositoryController.error.observe(this, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        fetchCharacters()
    }

    private fun fetchCharacters() {

        RepositoryController.charactersList.observe(this, Observer {
                characters ->
            adapter?.setData(characters)
        })

        RepositoryController.fetchAllCharacters()
    }
}