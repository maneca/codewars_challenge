package com.example.codewarschallenge.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.codewarschallenge.repository.ChallengesRepository
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.utils.AppResult
import com.example.codewarschallenge.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class CompletedChallengesViewModel(private val repository : ChallengesRepository) : ViewModel() {
    val showLoading = ObservableBoolean()
    val countriesList : MutableState<List<CompletedChallenge>> = mutableStateOf(ArrayList())
    val showError = SingleLiveEvent<String?>()

    init {
        getAllCountries()
    }

    fun getAllCountries() {

        showLoading.set(true)

        viewModelScope.launch {
            val result = repository.getAllCountries()

            showLoading.set(false)
            when(result){
                is AppResult.Success -> {
                    countriesList.value = result.successData
                    showError.value = null
                }
                is AppResult.Error -> showError.value = result.exception.message
            }
        }

    }
}