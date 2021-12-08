package com.example.codewarschallenge.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codewarschallenge.R
import com.example.codewarschallenge.db.model.ChallengeDetails
import com.example.codewarschallenge.repository.ChallengesRepository
import com.example.codewarschallenge.utils.ApiNotResponding
import com.example.codewarschallenge.utils.AppResult
import com.example.codewarschallenge.utils.NoConnectivityException
import kotlinx.coroutines.launch

class ChallengeDetailsViewModel(
    private val repository: ChallengesRepository
) : ViewModel() {

    var showLoading by mutableStateOf(false)
        private set
    var challengeDetails by mutableStateOf(ChallengeDetails())
        private set
    var showError by mutableStateOf<Int?>(null)
        private set


    fun getChallengeDetails(id: String) {
        showLoading = true

        viewModelScope.launch {
            val result = repository.getChallengeDetails(id)

            showLoading = false
            when (result) {
                is AppResult.Success -> {
                    challengeDetails = result.successData
                    showError = null
                }
                is AppResult.Error -> {
                    showError = when (result.exception) {
                        is ApiNotResponding ->
                            R.string.api_not_responding
                        is NoConnectivityException ->
                            R.string.no_network_connectivity
                        else ->
                            R.string.something_went_wrong
                    }
                }
                else -> showError = R.string.something_went_wrong
            }
        }
    }
}