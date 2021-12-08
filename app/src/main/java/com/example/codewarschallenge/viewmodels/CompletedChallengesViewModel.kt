package com.example.codewarschallenge.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codewarschallenge.R
import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.repository.ChallengesRepository
import com.example.codewarschallenge.utils.ApiNotResponding
import com.example.codewarschallenge.utils.AppResult
import com.example.codewarschallenge.utils.NoConnectivityException
import kotlinx.coroutines.launch

const val PAGE_SIZE = 200

class CompletedChallengesViewModel(private val repository : ChallengesRepository) : ViewModel() {
    var showLoading by mutableStateOf(false)
        private set

    var completedChallenges by mutableStateOf<List<CompletedChallenge>>(ArrayList())
        private set

    var showError by mutableStateOf<Int?>(null)
        private set

    var showWarning by mutableStateOf<Int?>(null)
        private set

    var page by mutableStateOf(1)
        private set

    private var totalPages: Int = 0

    init {
        getCompletedChallenges(0)
    }

    private fun getCompletedChallenges(pageToLoad: Int) {
        showLoading = true
        viewModelScope.launch {
            val result = repository.getCompletedChallenges(pageToLoad)

            showLoading = false
            when (result) {
                is AppResult.Success -> {
                    appendChallenges(result.successData!!.data)

                    if (pageToLoad == 0) {
                        page = result.successData.data.size / PAGE_SIZE
                    }
                    totalPages = result.successData.totalPages

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

    fun nextPage() {
        if (totalPages >= page) {
            getCompletedChallenges(page)
            page += 1
        } else {
            showWarning = R.string.no_more_data
        }

    }

    // Append challenges to the current list
    private fun appendChallenges(challenges: List<CompletedChallenge>){
        val current = ArrayList(completedChallenges)
        current.addAll(challenges)
        completedChallenges = current.sortedByDescending { it.completedAt }
    }
}