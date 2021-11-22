package com.example.codewarschallenge.api

import com.example.codewarschallenge.db.model.ChallengeDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChallengesApi {

    @GET("users/g964/code-challenges/completed")
    suspend fun getCompletedChallenges(@Query("page") page: Int): Response<ApiResponse>

    @GET("code-challenges/{challenge}")
    suspend fun getChallengeDetails(@Path("challenge") challenge : String): Response<ChallengeDetails>
}