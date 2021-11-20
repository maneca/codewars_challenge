package com.example.codewarschallenge.repository

import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.utils.AppResult

interface ChallengesRepository {

    suspend fun getCompletedChallenges(page: Int): AppResult<List<CompletedChallenge>>

}