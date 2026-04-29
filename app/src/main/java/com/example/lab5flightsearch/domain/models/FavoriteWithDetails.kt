package com.example.lab5flightsearch.domain.models

data class FavoriteWithDetails(
    val departureCode: String,
    val destinationCode: String,
    val departureName: String,
    val destinationName: String
)