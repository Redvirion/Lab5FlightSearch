package com.example.lab5flightsearch.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab5flightsearch.data.database.entities.FavoriteEntity
import com.example.lab5flightsearch.domain.models.FavoriteWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
    suspend fun deleteFavorite(departureCode: String, destinationCode: String)

    @Query("""
        SELECT favorite.departure_code as departureCode, 
               favorite.destination_code as destinationCode,
               dep.name as departureName,
               dest.name as destinationName
        FROM favorite
        INNER JOIN airport dep ON favorite.departure_code = dep.iata_code
        INNER JOIN airport dest ON favorite.destination_code = dest.iata_code
        ORDER BY favorite.id DESC
    """)
    fun getFavoritesWithDetails(): Flow<List<FavoriteWithDetails>>

}