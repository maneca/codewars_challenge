package com.example.codewarschallenge.api

import com.example.codewarschallenge.db.model.CompletedChallenge
import retrofit2.Response
import retrofit2.http.GET

interface ChallengesApi {

    @GET()
    suspend fun getCompletedChallenges(): Response<List<CompletedChallenge>>
}