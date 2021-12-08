package com.example.codewarschallenge.repository

import com.example.codewarschallenge.api.ApiResponse
import com.example.codewarschallenge.db.model.ChallengeDetails
import com.example.codewarschallenge.utils.AppResult

interface ChallengesRepository {

    suspend fun getCompletedChallenges(page: Int): AppResult<ApiResponse?>

    suspend fun getChallengeDetails(id: String): AppResult<ChallengeDetails>
}