package com.example.codewarschallenge.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codewarschallenge.db.model.ChallengeDetails
import com.example.codewarschallenge.repository.ChallengesRepository
import com.example.codewarschallenge.utils.AppResult
import com.example.codewarschallenge.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChallengeDetailsViewModel(private val id: String): ViewModel(), KoinComponent {

    private val repository by inject<ChallengesRepository>()

    val showLoading = mutableStateOf(false)
    var challengeDetails : MutableState<ChallengeDetails> = mutableStateOf(ChallengeDetails())
    val showError = SingleLiveEvent<String?>()

    init {
        getChallengeDetails()
    }

    private fun getChallengeDetails(){
        showLoading.value = true

        viewModelScope.launch {
            val result = repository.getChallengeDetails(id)

            showLoading.value = false
            when(result){
                is AppResult.Success -> {
                    challengeDetails.value = result.successData
                    showError.value = null
                }
                is AppResult.Error -> showError.value = result.exception.message
            }
        }
    }
}