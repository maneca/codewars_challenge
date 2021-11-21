package com.example.codewarschallenge.repository

import android.content.Context
import android.util.Log
import com.example.codewarschallenge.api.ChallengesApi
import com.example.codewarschallenge.db.dao.ChallengesDao
import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.utils.ApiErrorUtils
import com.example.codewarschallenge.utils.AppResult
import com.example.codewarschallenge.utils.NetworkManager.isOnline
import com.example.codewarschallenge.utils.ResponseHandler.handleApiError
import com.example.codewarschallenge.utils.ResponseHandler.handleSuccess
import com.example.codewarschallenge.utils.noNetworkConnectivityError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChallengesRepositoryImp(
    private val api : ChallengesApi,
    private val context : Context,
    private val dao : ChallengesDao
) : ChallengesRepository{

    override suspend fun getCompletedChallenges(page: Int, forceUpdate : Boolean): AppResult<List<CompletedChallenge>> {
        val dbValues = getCompletedChallengesFromDatabase()

        when {
            dbValues.isNotEmpty() && !forceUpdate -> {
                Log.d("Codewars", "from db")
                return AppResult.Success(dbValues)
            }
            isOnline(context) -> {
                Log.d("Codewars", "from network")
                return try {
                    val response = api.getCompletedChallenges(page)

                    if (response.isSuccessful){
                        response.body()?.let {
                            withContext(Dispatchers.IO) { dao.addCompletedChallenges(it.data)}
                        }
                        AppResult.Success(response.body()!!.data)
                    } else{
                        AppResult.Error(Exception(ApiErrorUtils.parseError(response).message))
                    }
                }catch (e: Exception) {
                    AppResult.Error(e)
                }
            }
            else -> {
                return  context.noNetworkConnectivityError()
            }
        }
    }

    private suspend fun getCompletedChallengesFromDatabase(): List<CompletedChallenge>{
        return withContext(Dispatchers.IO) { dao.getCompletedChallenges() }
    }
}