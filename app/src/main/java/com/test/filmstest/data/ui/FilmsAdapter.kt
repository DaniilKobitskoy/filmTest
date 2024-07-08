package com.test.filmstest.data.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.filmstest.R
import com.test.filmstest.data.model.FilmEntity

class FilmsAdapter(
    private val films: List<FilmEntity>,
    private val onItemClicked: (FilmEntity) -> Unit
) : RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filmTitle: TextView = itemView.findViewById(R.id.titleTextView)
        val director: TextView = itemView.findViewById(R.id.directorTextView)
        val date: TextView = itemView.findViewById(R.id.releaseDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = films[position]
        holder.filmTitle.text = film.title
        holder.director.text = film.director
        holder.date.text = film.release_date
        holder.itemView.setOnClickListener {
            onItemClicked(film)
        }
    }

    override fun getItemCount() = films.size
}
