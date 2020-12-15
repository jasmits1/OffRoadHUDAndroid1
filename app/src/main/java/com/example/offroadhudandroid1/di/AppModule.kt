package com.example.offroadhudandroid1.di

import android.content.Context
import androidx.room.Room
import com.example.offroadhudandroid1.db.OffRoadHudDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDb(
            @ApplicationContext app: Context
    ) = Room.databaseBuilder(
            app,
            OffRoadHudDb::class.java,
            "off_road_hud_db"
    ).build()

    @Singleton
    @Provides
    fun provideLocationDao(db: OffRoadHudDb) = db.locationDao()
}