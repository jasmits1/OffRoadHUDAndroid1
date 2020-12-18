package com.example.offroadhudandroid1.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.offroadhudandroid1.Model.InclineModel
import com.example.offroadhudandroid1.Model.LocationModel
import com.example.offroadhudandroid1.Model.RouteModel
import okhttp3.Route

/**
 * Main database description
 */
@Database(
    entities = [
    InclineModel::class,
    LocationModel::class,
    RouteModel::class],
    version = 1,
    exportSchema = false
)
abstract class OffRoadHudDb : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun routeDao(): RouteDao
}