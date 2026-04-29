package com.example.lab5flightsearch.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lab5flightsearch.R
import com.example.lab5flightsearch.data.database.AppDatabase
import com.example.lab5flightsearch.data.repository.FlightSearchRepositoryImpl
import com.example.lab5flightsearch.domain.models.Airport
import com.example.lab5flightsearch.presentation.ui.adapters.AirportSuggestionAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var actvDeparture: AutoCompleteTextView
    private lateinit var actvDestination: AutoCompleteTextView
    private lateinit var repository: FlightSearchRepositoryImpl
    private var selectedDeparture: Airport? = null
    private var selectedDestination: Airport? = null
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actvDeparture = findViewById(R.id.actvDeparture)
        actvDestination = findViewById(R.id.actvDestination)
        val btnAddFlight = findViewById<android.widget.Button>(R.id.btnAddFlight)
        val btnViewFavorites = findViewById<android.widget.Button>(R.id.btnViewFavorites)

        val database = AppDatabase.getInstance(this)
        repository = FlightSearchRepositoryImpl(database)

        actvDeparture.setAdapter(AirportSuggestionAdapter(this, emptyList()))
        actvDestination.setAdapter(AirportSuggestionAdapter(this, emptyList()))

        actvDeparture.setOnItemClickListener { _, _, position, _ ->
            selectedDeparture = (actvDeparture.adapter as AirportSuggestionAdapter).getItem(position)
            selectedDeparture?.let {
                actvDeparture.setText(getString(R.string.airport_format, it.iataCode, it.name))
            }
        }

        actvDestination.setOnItemClickListener { _, _, position, _ ->
            selectedDestination = (actvDestination.adapter as AirportSuggestionAdapter).getItem(position)
            selectedDestination?.let {
                actvDestination.setText(getString(R.string.airport_format, it.iataCode, it.name))
            }
        }

        actvDeparture.addTextChangedListener(createTextWatcher { query ->
            searchAirports(query) { airports ->
                updateSuggestions(actvDeparture, airports)
            }
        })

        actvDestination.addTextChangedListener(createTextWatcher { query ->
            searchAirports(query) { airports ->
                updateSuggestions(actvDestination, airports)
            }
        })

        btnAddFlight.setOnClickListener { addToFavorites() }
        btnViewFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }

    private fun createTextWatcher(onQuery: (String) -> Unit): android.text.TextWatcher {
        return object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.length < 2) {
                    updateSuggestions(actvDeparture, emptyList())
                    updateSuggestions(actvDestination, emptyList())
                    return
                }
                onQuery(query)
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        }
    }

    private fun searchAirports(query: String, onResult: (List<Airport>) -> Unit) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(300)
            val result = repository.searchAirports(query)
            result.onSuccess { airports ->
                onResult(airports)
            }.onFailure {
                onResult(emptyList())
                Toast.makeText(this@MainActivity, getString(R.string.error_search), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSuggestions(autoComplete: AutoCompleteTextView, airports: List<Airport>) {
        val adapter = AirportSuggestionAdapter(this, airports)
        autoComplete.setAdapter(adapter)
        adapter.notifyDataSetChanged()
        if (airports.isNotEmpty()) {
            autoComplete.showDropDown()
        } else {
            autoComplete.dismissDropDown()
        }
    }

    private fun addToFavorites() {
        if (selectedDeparture == null || selectedDestination == null) {
            Toast.makeText(this, getString(R.string.error_select_both), Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedDeparture!!.iataCode == selectedDestination!!.iataCode) {
            Toast.makeText(this, getString(R.string.error_same_airport), Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val result = repository.addToFavorites(selectedDeparture!!.iataCode, selectedDestination!!.iataCode)
            if (result.isSuccess) {
                Toast.makeText(this@MainActivity, getString(R.string.flight_added), Toast.LENGTH_SHORT).show()
                selectedDeparture = null
                selectedDestination = null
                actvDeparture.setText("")
                actvDestination.setText("")
            } else {
                Toast.makeText(this@MainActivity, getString(R.string.error_add), Toast.LENGTH_SHORT).show()
            }
        }
    }
}