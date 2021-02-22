package com.example.rickandmortygo.presentation.collection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortygo.MainActivity
import com.example.rickandmortygo.R
import com.example.rickandmortygo.presentation.characters.CharactersAdapter
import com.example.rickandmortygo.presentation.scanner.ScanActivity
import com.example.rickandmortygo.repository.RepositoryController
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import kotlinx.android.synthetic.main.fragment_characters.*
import kotlinx.android.synthetic.main.fragment_collection.*


class CollectionFragment : Fragment() {

    lateinit var adapter: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener{scanQRCode()}
        rv_collection_list.layoutManager = GridLayoutManager(activity, 3)
        adapter = CharactersAdapter()
        rv_collection_list.adapter = adapter
        rv_collection_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        RepositoryController.collectionList.observe(this, Observer {
                characters ->
            adapter?.setData(characters)
        })

        RepositoryController.fetchCollection(context!!)
    }

    fun scanQRCode(){
        val integrator = IntentIntegrator(activity).apply {
            captureActivity = CaptureActivity::class.java
            setOrientationLocked(false)
            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            setPrompt("Scanning Code")
        }
        integrator.initiateScan()
    }

    companion object {
        const val REQUEST_CODE_SCAN = 2;
    }
}