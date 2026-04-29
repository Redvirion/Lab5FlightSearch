package com.example.lab5flightsearch.data.repository

import com.example.lab5flightsearch.domain.models.Airport
import com.example.lab5flightsearch.domain.models.FavoriteWithDetails
import kotlinx.coroutines.flow.Flow

interface FlightSearchRepository {
    suspend fun searchAirports(query: String): Result<List<Airport>>
    fun getFavoritesWithDetails(): Flow<List<FavoriteWithDetails>>
    suspend fun addToFavorites(departureCode: String, destinationCode: String): Result<Unit>
    suspend fun removeFromFavorites(departureCode: String, destinationCode: String): Result<Unit>
}