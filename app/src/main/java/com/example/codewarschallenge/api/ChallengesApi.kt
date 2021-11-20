package com.example.codewarschallenge.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ChallengesApi {

    @GET("users/g964/code-challenges/completed")
    suspend fun getCompletedChallenges(@Query("page") page: Int): Response<ApiResponse>
}