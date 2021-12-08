package com.example.codewarschallenge.repository

import android.util.Log
import com.example.codewarschallenge.api.ApiResponse
import com.example.codewarschallenge.api.ChallengesApi
import com.example.codewarschallenge.db.dao.ChallengesDao
import com.example.codewarschallenge.db.model.ChallengeDetails
import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChallengesRepositoryImp(
    private val api : ChallengesApi,
    private val dao : ChallengesDao
) : ChallengesRepository{

    override suspend fun getCompletedChallenges(page: Int): AppResult<ApiResponse?> {
        val dbValues = getCompletedChallengesFromDatabase()

        if (page == 0 && dbValues.isNotEmpty()) {
            // fetch from the local database
            Log.d("Codewars", "from db")

            return AppResult.Success(ApiResponse(0, dbValues.size, dbValues))
        } else {
            return try {
                val response = api.getCompletedChallenges(page)

                if (response.isSuccessful) {
                    response.body()?.let {
                        dao.addCompletedChallenges(it.data)
                    }
                    AppResult.Success(response.body())
                } else {
                    Log.d("Codewars", ApiErrorUtils.parseError(response).message)
                    AppResult.Error(ApiNotResponding())
                }
            } catch (ex: NoConnectivityException) {
                ex.message?.let { Log.d("Codewars", it) }
                AppResult.Error(NoConnectivityException())
            } catch (e: Exception) {
                e.message?.let { Log.d("Codewars", it) }
                AppResult.Error(UnknownException())
            }
        }
    }

    private suspend fun getCompletedChallengesFromDatabase(): List<CompletedChallenge>{
        return withContext(Dispatchers.IO) { dao.getCompletedChallenges() }
    }

    override suspend fun getChallengeDetails(id: String): AppResult<ChallengeDetails> {
        return try {
            val response = api.getChallengeDetails(id)

            if (response.isSuccessful) {
                AppResult.Success(response.body()!!)
            } else {
                Log.d("Codewars", ApiErrorUtils.parseError(response).message)
                AppResult.Error(ApiNotResponding())
            }
        } catch (ex: NoConnectivityException) {
            ex.message?.let { Log.d("Codewars", it) }
            AppResult.Error(NoConnectivityException())
        } catch (e: Exception) {
            e.message?.let { Log.d("Codewars", it) }
            AppResult.Error(UnknownException())
        }
    }
}