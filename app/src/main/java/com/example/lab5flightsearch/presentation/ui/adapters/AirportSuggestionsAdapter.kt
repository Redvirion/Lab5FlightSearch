package com.example.lab5flightsearch.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.lab5flightsearch.R
import com.example.lab5flightsearch.domain.models.Airport

class AirportSuggestionAdapter(context: Context, private val airports: List<Airport>) :
    ArrayAdapter<Airport>(context, R.layout.item_airport_suggestion, airports) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_airport_suggestion, parent, false)
        val airport = getItem(position)
        view.findViewById<TextView>(R.id.tvAirportName).text = airport?.name ?: ""
        view.findViewById<TextView>(R.id.tvIataCode).text = airport?.iataCode ?: ""

        val tvPassengers = view.findViewById<TextView>(R.id.tvPassengers)
        airport?.let {
            tvPassengers.text = context.getString(R.string.passengers_per_year, it.passengers)
        } ?: run {
            tvPassengers.text = ""
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}