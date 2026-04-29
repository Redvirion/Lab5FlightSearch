package com.example.lab5flightsearch.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.lab5flightsearch.data.database.entities.AirportEntity

@Dao
interface AirportDao {

    @Query("""
        SELECT * FROM airport 
        WHERE iata_code LIKE :query OR name LIKE :query 
        ORDER BY passengers DESC 
        LIMIT 30
    """)
    suspend fun searchAirports(query: String): List<AirportEntity>

    @Query("SELECT * FROM airport ORDER BY name")
    suspend fun getAllAirports(): List<AirportEntity>


}