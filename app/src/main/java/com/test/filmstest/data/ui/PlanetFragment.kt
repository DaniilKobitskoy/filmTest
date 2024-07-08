package com.test.filmstest.data.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.facebook.shimmer.ShimmerFrameLayout
import com.test.filmstest.data.viewmodel.PlanetViewModel
import com.test.filmstest.databinding.FragmentPlanetBinding

class PlanetFragment : Fragment() {

    private var _binding: FragmentPlanetBinding? = null
    private val binding get() = _binding!!
    private val planetViewModel: PlanetViewModel by viewModels()

    private lateinit var shimmerViewContainer: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanetBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val planetUrl = arguments?.getString("PLANET_URL") ?: return

        shimmerViewContainer = binding.shimmerViewContainer
        shimmerViewContainer.startShimmer()

        planetViewModel.fetchPlanet(planetUrl)

        planetViewModel.planet.observe(viewLifecycleOwner) { planet ->
            planet?.let {
                binding.planetName.text = it.name
                binding.planetRotationPeriod.text = "Rotation Period: ${it.rotation_period}"
                binding.orbitalPeriod.text = "Orbital Period: ${it.orbital_period} "
                binding.diameter.text = "Diameter: ${it.diameter} "
                binding.climate.text = "Climate: ${it.climate}"
                binding.gravity.text = "Gravity: ${it.gravity}"
                binding.terrain.text = "Terrain: ${it.terrain} "
                binding.surfaceWater.text = "Surface Water: ${it.surface_water}"
                binding.population.text = "Population: ${it.population}"
                shimmerViewContainer.stopShimmer()
                shimmerViewContainer.visibility = View.GONE
                binding.contentView.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
