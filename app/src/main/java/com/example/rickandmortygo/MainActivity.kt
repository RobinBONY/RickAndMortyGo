package com.example.rickandmortygo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rickandmortygo.presentation.characters.CharactersFragment
import com.example.rickandmortygo.presentation.collection.CollectionFragment
import com.example.rickandmortygo.presentation.episodes.EpisodesFragment
import com.example.rickandmortygo.presentation.locations.LocationsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //permet de ne pas recharger le fragment deja selectionné
    private var selectedTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = resources.getString(R.string.collection)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //ajout du premier fragment
        supportFragmentManager.beginTransaction().add(R.id.content,
            CollectionFragment()
        ).commit()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->

        if (selectedTab != menuItem.itemId) {
            selectedTab = menuItem.itemId
            when (menuItem.itemId) {
                R.id.navigation_collection -> {
                    val fragment = CollectionFragment()
                    title = resources.getString(R.string.collection)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content, fragment)
                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_characters -> {
                    val fragment = CharactersFragment()
                    title = resources.getString(R.string.characters)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content, fragment)
                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_episodes -> {
                    val fragment = EpisodesFragment()
                    title = resources.getString(R.string.episodes)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content, fragment)
                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_places -> {
                    val fragment = LocationsFragment()
                    title = resources.getString(R.string.locations)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content, fragment)
                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
        }
        false
    }
}