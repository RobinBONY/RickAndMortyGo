package com.example.rickandmortygo.presentation.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortygo.R
import com.example.rickandmortygo.data.model.Character
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_character.view.*

class CharactersAdapter :
    RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {

    private var charactersList = ArrayList<Character>()

    inner class CharactersViewHolder(private val charactersView: View) :
        RecyclerView.ViewHolder(charactersView) {

        fun bind(character: Character) {
            Picasso.get().load(character.image).into(charactersView.characterImageView)
            charactersView.characterName.text = character.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_character, parent, false)
        return CharactersViewHolder(v)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(charactersList[position])
    }

    override fun getItemCount(): Int {
        return charactersList.size
    }

    fun setData(characters: List<Character>?) {
        if (characters == null) {
            return
        }
        this.charactersList.addAll(characters)
        notifyDataSetChanged()
    }

    fun resetData(){
        this.charactersList.clear()
    }
}