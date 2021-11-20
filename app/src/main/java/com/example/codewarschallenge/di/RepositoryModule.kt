package com.example.codewarschallenge.di

import android.content.Context
import com.example.codewarschallenge.api.ChallengesApi
import com.example.codewarschallenge.db.dao.ChallengesDao
import com.example.codewarschallenge.repository.ChallengesRepository
import com.example.codewarschallenge.repository.ChallengesRepositoryImp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    fun provideCountriesRepository(api: ChallengesApi, context: Context, dao: ChallengesDao): ChallengesRepository {
        return ChallengesRepositoryImp(api, context, dao)
    }

    single { provideCountriesRepository(get(), androidContext(), get()) }
}