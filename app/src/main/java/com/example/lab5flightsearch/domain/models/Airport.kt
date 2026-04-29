package com.example.lab5flightsearch.domain.models

data class Airport(
    val id: Int,
    val iataCode: String,
    val name: String,
    val passengers: Int
)