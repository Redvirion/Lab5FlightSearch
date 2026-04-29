package com.example.lab5flightsearch.presentation.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.lab5flightsearch.R
import com.example.lab5flightsearch.data.database.AppDatabase
import com.example.lab5flightsearch.data.repository.FlightSearchRepositoryImpl
import com.example.lab5flightsearch.databinding.ActivityFavoritesBinding
import com.example.lab5flightsearch.domain.models.FavoriteWithDetails
import com.example.lab5flightsearch.presentation.ui.adapters.FavoritesAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var adapter: FavoritesAdapter
    private lateinit var repository: FlightSearchRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val database = AppDatabase.getInstance(this)
        repository = FlightSearchRepositoryImpl(database)

        adapter = FavoritesAdapter { favorite ->
            deleteFavorite(favorite)
        }
        binding.rvFavorites.adapter = adapter

        loadFavorites()
    }

    private fun loadFavorites() {
        lifecycleScope.launch {
            repository.getFavoritesWithDetails()
                .onEach { favorites ->
                    if (favorites.isEmpty()) {
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.rvFavorites.visibility = View.GONE
                    } else {
                        binding.tvEmpty.visibility = View.GONE
                        binding.rvFavorites.visibility = View.VISIBLE
                        adapter.submitList(favorites)
                    }
                }
                .launchIn(this)
        }
    }

    private fun deleteFavorite(favorite: FavoriteWithDetails) {
        lifecycleScope.launch {
            val result = repository.removeFromFavorites(favorite.departureCode, favorite.destinationCode)
            if (result.isSuccess) {
                Toast.makeText(this@FavoritesActivity, getString(R.string.deleted_from_favorites), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@FavoritesActivity, getString(R.string.error_delete), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}