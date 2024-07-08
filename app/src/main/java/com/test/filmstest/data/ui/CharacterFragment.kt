package com.test.filmstest.data.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.test.filmstest.data.viewmodel.CharacterViewModel
import com.test.filmstest.R
import com.test.filmstest.databinding.FragmentCharacterBinding

class CharacterFragment : Fragment() {

    private var _binding: FragmentCharacterBinding? = null
    private val binding get() = _binding!!
    private val characterViewModel: CharacterViewModel by viewModels()

    private lateinit var shimmerViewContainer: ShimmerFrameLayout
    private lateinit var charactersAdapter: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmId = arguments?.getLong("FILM_ID") ?: 0L
        val filmTitle = arguments?.getString("FILM_TITLE") ?: ""
        val characterUrls = arguments?.getStringArray("FILM_char")?.toList() ?: emptyList()

        shimmerViewContainer = binding.shimmerViewContainer
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        activity?.title = filmTitle
        (requireActivity() as? MainActivity)?.showSystemUI()

        if (filmId != 0L) {
            shimmerViewContainer.startShimmer()
            characterViewModel.fetchAndSaveCharactersForFilm(filmId, characterUrls)

            characterViewModel.getCharactersByFilmId(filmId)
                .observe(viewLifecycleOwner) { characters ->
                    if (characters.isNotEmpty()) {
                        charactersAdapter = CharactersAdapter(characters) { character ->
                            val bundle = Bundle().apply {
                                putString("PLANET_URL", character.homeworld)
                            }
                            val fragment = PlanetFragment().apply { arguments = bundle }
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_view, fragment)
                                .addToBackStack(null)
                                .commit()
                        }
                        recyclerView.adapter = charactersAdapter
                        shimmerViewContainer.stopShimmer()
                        shimmerViewContainer.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (requireActivity() as? MainActivity)?.hideSystemUI()
    }

}
