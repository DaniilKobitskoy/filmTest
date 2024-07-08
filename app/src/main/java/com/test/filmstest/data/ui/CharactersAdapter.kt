package com.test.filmstest.data.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.filmstest.R
import com.test.filmstest.data.model.CharacterEntity

class CharactersAdapter(
    private val characters: List<CharacterEntity>,
    private val onItemClick: (CharacterEntity) -> Unit
) : RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.bind(character, onItemClick)
    }

    override fun getItemCount(): Int = characters.size

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.character_name)
        private val heightTextView: TextView = itemView.findViewById(R.id.character_height)
        private val massTextView: TextView = itemView.findViewById(R.id.character_mass)
        private val hairTextView: TextView = itemView.findViewById(R.id.character_hair)
        private val skinTextView: TextView = itemView.findViewById(R.id.character_skin)
        private val eyeTextView: TextView = itemView.findViewById(R.id.character_eye)
        private val birthTextView: TextView = itemView.findViewById(R.id.character_birth)
        private val genderTextView: TextView = itemView.findViewById(R.id.character_genfer)

        @SuppressLint("SetTextI18n")
        fun bind(character: CharacterEntity, onItemClick: (CharacterEntity) -> Unit) {
            nameTextView.text = character.name
            heightTextView.text = "Height: ${character.height}"
            massTextView.text = "Mass: ${character.mass}"
            hairTextView.text = "Hair Color: ${character.hair_color} "
            skinTextView.text = "Skin Color: ${character.skin_color}"
            eyeTextView.text = "Eye Color: ${character.eye_color}"
            birthTextView.text = "Birth Year: ${character.birth_year}"
            genderTextView.text = "Gender: ${character.gender}"

            itemView.setOnClickListener {
                onItemClick(character)
            }
        }
    }
}
