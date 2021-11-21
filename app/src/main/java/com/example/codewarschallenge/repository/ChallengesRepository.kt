package com.example.codewarschallenge.repository

import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.utils.AppResult

interface ChallengesRepository {

    suspend fun getCompletedChallenges(page: Int, forceUpdate : Boolean = false): AppResult<List<CompletedChallenge>>

}