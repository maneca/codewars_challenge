package com.example.codewarschallenge.di

import android.app.Application
import androidx.room.Room
import com.example.codewarschallenge.db.ChallengesDatabase
import com.example.codewarschallenge.db.dao.ChallengesDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(application: Application): ChallengesDatabase {

        return Room.databaseBuilder(application, ChallengesDatabase::class.java, "challenges")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideChallengesDao(database: ChallengesDatabase): ChallengesDao {
        return database.challengesDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideChallengesDao(get()) }
}