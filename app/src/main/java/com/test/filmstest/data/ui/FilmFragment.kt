package com.test.filmstest.data.ui

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.test.filmstest.R
import com.test.filmstest.data.viewmodel.FilmViewModel
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
        val searchView = binding.searchView
        recyclerView.layoutManager = LinearLayoutManager(context)

        setupSearchView(binding.searchView)

        filmViewModel.filteredFilms.observe(viewLifecycleOwner) { films ->
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
                searchView.visibility = View.VISIBLE
                binding.noResultsTextView.visibility = View.GONE
            } else {
                if (::filmsAdapter.isInitialized) {
                    binding.noResultsTextView.visibility = View.VISIBLE
                }
                recyclerView.visibility = View.GONE
                searchView.visibility = View.VISIBLE
                filmViewModel.fetchAndInsertFilmsIfNotSearched()
            }

        }


    }

    private fun setupSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val queryText = newText.orEmpty()
                filmViewModel.searchFilmsByTitle(queryText)

                return true
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
