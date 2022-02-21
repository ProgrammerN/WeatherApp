package com.dvt.weatherapp.activities.home.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dvt.weatherapp.adapters.FavoriteWeatherItemAdapter
import com.dvt.weatherapp.databinding.FragmentFavoritesBinding
import com.dvt.weatherapp.extentions.toast
import com.dvt.weatherapp.viewmodels.LocalWeatherViewModel
import timber.log.Timber

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val localWeatherViewModel: LocalWeatherViewModel by viewModels()
    private lateinit var mWeatherItemAdapter: FavoriteWeatherItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        showFavorites()
        return root
    }

    private fun showFavorites() {
        localWeatherViewModel.getAllCurrentWeather()?.observe(viewLifecycleOwner) {

            if (it.isNotEmpty()){
                mWeatherItemAdapter = FavoriteWeatherItemAdapter(requireContext(), it)
                Timber.d(it[0].name.toString())
                binding.recyclerView.adapter = mWeatherItemAdapter
            }else{
                requireContext().toast("No favorites found")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}