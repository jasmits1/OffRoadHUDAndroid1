package com.example.offroadhudandroid1.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.offroadhudandroid1.Model.LocationModel

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationModel)

    @Query("SELECT * FROM locationmodel")
    suspend fun getAll(): List<LocationModel>

    @Query("SELECT * FROM locationmodel WHERE routename = :routeName")
    suspend fun getLocationsForRoute(routeName: String): List<LocationModel>
}