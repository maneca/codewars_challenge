package com.example.codewarschallenge.repository

import android.content.Context
import android.util.Log
import com.example.codewarschallenge.api.ChallengesApi
import com.example.codewarschallenge.db.dao.ChallengesDao
import com.example.codewarschallenge.db.model.ChallengeDetails
import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.utils.*
import com.example.codewarschallenge.utils.NetworkManager.isOnline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChallengesRepositoryImp(
    private val api : ChallengesApi,
    private val context : Context,
    private val dao : ChallengesDao
) : ChallengesRepository{

    private var totalPages: Int = 0

    override suspend fun getCompletedChallenges(page: Int): AppResult<List<CompletedChallenge>> {
        val dbValues = getCompletedChallengesFromDatabase()

        when {
            totalPages > 0 && page >= totalPages -> {
                return context.reachedTheEnd()
            }
            isOnline(context) -> {
                Log.d("Codewars", "from network")
                return try {
                    val response = api.getCompletedChallenges(page)

                    if (response.isSuccessful){
                        response.body()?.let {
                            totalPages = it.totalPages
                            withContext(Dispatchers.IO) { dao.addCompletedChallenges(it.data)}
                        }
                        AppResult.Success(response.body()!!.data)
                    } else{
                        Log.d("Codewars", ApiErrorUtils.parseError(response).message)
                        context.apiIsNotResponding()
                    }
                }catch (e: Exception) {
                    e.message?.let { Log.d("Codewars", it) }
                    context.unknownException()
                }
            }
            dbValues.isNotEmpty() -> {
                Log.d("Codewars", "from db")
                return AppResult.Success(dbValues)
            }
            else -> {
                return context.noNetworkConnectivityError()
            }
        }
    }

    private suspend fun getCompletedChallengesFromDatabase(): List<CompletedChallenge>{
        return withContext(Dispatchers.IO) { dao.getCompletedChallenges() }
    }

    override suspend fun getChallengeDetails(id: String): AppResult<ChallengeDetails> {
        return if(isOnline(context)){
            try{
                val response = api.getChallengeDetails(id)

                if (response.isSuccessful){
                    AppResult.Success(response.body()!!)
                } else{
                    Log.d("Codewars", ApiErrorUtils.parseError(response).message)
                    context.apiIsNotResponding()
                }
            }
            catch (e: Exception) {
                e.message?.let { Log.d("Codewars", it) }
                context.unknownException()
            }
        }else{
            context.noNetworkConnectivityError()
        }
    }
}