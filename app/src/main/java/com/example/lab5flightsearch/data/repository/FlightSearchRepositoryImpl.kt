package com.example.lab5flightsearch.data.repository

import com.example.lab5flightsearch.data.database.AppDatabase
import com.example.lab5flightsearch.data.database.entities.AirportEntity
import com.example.lab5flightsearch.data.database.entities.FavoriteEntity
import com.example.lab5flightsearch.domain.models.Airport
import com.example.lab5flightsearch.domain.models.FavoriteWithDetails
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class FlightSearchRepositoryImpl(
    private val database: AppDatabase
) : FlightSearchRepository {

    private val airportDao = database.airportDao()
    private val favoriteDao = database.favoriteDao()

    override suspend fun searchAirports(query: String): Result<List<Airport>> {
        return try {
            val formattedQuery = "%${query.trim()}%"
            val entities = airportDao.searchAirports(formattedQuery)
            val airports = entities.map { it.toDomainModel() }
            Result.success(airports)
        } catch (e: Exception) {
            Result.failure(IOException("Ошибка поиска аэропортов", e))
        }
    }

    override fun getFavoritesWithDetails(): Flow<List<FavoriteWithDetails>> {
        return favoriteDao.getFavoritesWithDetails()
    }

    override suspend fun addToFavorites(departureCode: String, destinationCode: String): Result<Unit> {
        return try {
            val favorite = FavoriteEntity(departureCode = departureCode, destinationCode = destinationCode)
            favoriteDao.insertFavorite(favorite)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(IOException("Ошибка добавления в избранное", e))
        }
    }

    override suspend fun removeFromFavorites(departureCode: String, destinationCode: String): Result<Unit> {
        return try {
            favoriteDao.deleteFavorite(departureCode, destinationCode)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(IOException("Ошибка удаления из избранного", e))
        }
    }

    private fun AirportEntity.toDomainModel() = Airport(
        id = id,
        iataCode = iataCode,
        name = name,
        passengers = passengers
    )
}