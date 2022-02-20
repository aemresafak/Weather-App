package com.example.weatherprojecttry_1

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherprojecttry_1.data.RecentQueryProvider
import com.example.weatherprojecttry_1.databinding.ActivityMainBinding
import com.example.weatherprojecttry_1.ui.cities.CitiesViewModel
import com.example.weatherprojecttry_1.ui.today.TodayViewModel
import com.example.weatherprojecttry_1.utils.PagerAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var recentSuggestions: SearchRecentSuggestions
    private val todayViewModel: TodayViewModel by viewModels()
    private val citiesViewModel: CitiesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupPager()

        citiesViewModel.mutableLiveDataClickedWeather.observe(this) {
            binding.viewPager2.setCurrentItem(2, true)
        }

    }

    private fun setupPager() {
        binding.viewPager2.adapter = PagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab: TabLayout.Tab, i: Int ->
            tab.text = when (i) {
                0 -> "Today"
                1 -> "Cities"
                else -> "City Details"
            }
        }.attach()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.menuItemSearch)?.actionView as SearchView
        searchView.isIconifiedByDefault = false
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == Intent.ACTION_SEARCH) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            recentSuggestions.saveRecentQuery(query, null)
            todayViewModel.fetchCurrentWeather(query!!)
        }
    }
}











