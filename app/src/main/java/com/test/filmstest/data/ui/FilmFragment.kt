package com.test.filmstest.data.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.test.filmstest.data.viewmodel.FilmViewModel
import com.test.filmstest.R
import com.test.filmstest.databinding.FragmentFilmBinding


class FilmFragment : Fragment() {
    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!
    private val filmViewModel: FilmViewModel by viewModels()

    private lateinit var shimmerViewContainer: ShimmerFrameLayout
    private lateinit var filmsAdapter: FilmsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shimmerViewContainer = binding.shimmerViewContainer
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        filmViewModel.allFilms.observe(viewLifecycleOwner) { films ->
            if (films.isNotEmpty()) {
                filmsAdapter = FilmsAdapter(films) { film ->
                    val bundle = Bundle().apply {
                        putLong("FILM_ID", film.id)
                        putString("FILM_TITLE", film.title)
                        putStringArray("FILM_char", film.characters.toTypedArray())
                    }
                    val fragment = CharacterFragment().apply { arguments = bundle }
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, fragment)
                        .addToBackStack(null)
                        .commit()
                }
                recyclerView.adapter = filmsAdapter
                shimmerViewContainer.stopShimmer()
                shimmerViewContainer.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            } else {
                filmViewModel.fetchAndInsertFilms()
                shimmerViewContainer.startShimmer()
                shimmerViewContainer.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}