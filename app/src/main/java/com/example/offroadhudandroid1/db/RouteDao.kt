package com.example.offroadhudandroid1.db

import androidx.room.*
import com.example.offroadhudandroid1.Model.RouteModel

@Dao
interface RouteDao {

    @Query("SELECT * FROM routemodel WHERE isactive = 1 LIMIT 1")
    suspend fun findActiveRoute(): RouteModel?

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(routeModel: RouteModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(route: RouteModel)

    @Query("SELECT * FROM routemodel")
    suspend fun getAll(): List<RouteModel>
}