package com.example.codewarschallenge.di

import com.example.codewarschallenge.api.ChallengesApi
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    fun provideChallengesApi(retrofit: Retrofit): ChallengesApi {
        return retrofit.create(ChallengesApi::class.java)
    }

    single { provideChallengesApi(get()) }
}