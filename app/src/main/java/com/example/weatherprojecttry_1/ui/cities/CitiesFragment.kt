package com.example.weatherprojecttry_1.ui.cities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherprojecttry_1.data.db.models.WeatherEntity
import com.example.weatherprojecttry_1.databinding.FragmentCitiesBinding
import com.example.weatherprojecttry_1.ui.cities.recyclerViewUtils.CitiesAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CitiesFragment : Fragment(), CitiesAdapter.ItemClickHandler {

    private var _binding: FragmentCitiesBinding? = null
    private val binding get() = _binding!!


    val adapter = CitiesAdapter(this)

    @Inject
    lateinit var layoutManager: LinearLayoutManager
    private val viewModel: CitiesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCitiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    private fun setupDeleteOnSwipe() {
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteWeather(adapter.weathers[viewHolder.adapterPosition])
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
        setupDeleteOnSwipe()
        viewModel.liveDataWeatherList.observe(requireActivity()) {
            adapter.weathers = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun handleClick(weatherEntity: WeatherEntity) {
        viewModel.mutableLiveDataClickedWeather.value = weatherEntity
    }
}