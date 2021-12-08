package com.example.codewarschallenge.di

import com.example.codewarschallenge.api.ChallengesApi
import com.example.codewarschallenge.db.dao.ChallengesDao
import com.example.codewarschallenge.repository.ChallengesRepository
import com.example.codewarschallenge.repository.ChallengesRepositoryImp
import org.koin.dsl.module

val repositoryModule = module {

    fun provideChallengesRepository(api: ChallengesApi, dao: ChallengesDao): ChallengesRepository {
        return ChallengesRepositoryImp(api, dao)
    }

    single { provideChallengesRepository(get(), get()) }
}