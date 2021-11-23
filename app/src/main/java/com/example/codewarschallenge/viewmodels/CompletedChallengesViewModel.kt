package com.example.codewarschallenge.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.codewarschallenge.repository.ChallengesRepository
import androidx.lifecycle.viewModelScope
import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.utils.AppResult
import com.example.codewarschallenge.utils.SingleLiveEvent
import kotlinx.coroutines.launch

const val PAGE_SIZE = 200

class CompletedChallengesViewModel(private val repository : ChallengesRepository) : ViewModel() {
    val showLoading = mutableStateOf(false)
    val completedChallenges : MutableState<List<CompletedChallenge>> = mutableStateOf(ArrayList())
    val showError = SingleLiveEvent<String?>()
    val showWarning =  SingleLiveEvent<String?>()

    val page = mutableStateOf(1)
    var challengesScrollPosition = 0

    init {
        getCompletedChallenges(0)
    }

    private fun getCompletedChallenges(page : Int) {
        showLoading.value = true

        viewModelScope.launch {
            val result = repository.getCompletedChallenges(page)

            showLoading.value = false
            when(result){
                is AppResult.Success -> {
                    appendChallenges(result.successData)
                    showError.value = null
                }
                is AppResult.Error -> showError.value = result.exception.message
                is AppResult.Warning -> showWarning.value = result.message
            }
        }
    }

    fun nextPage(){
        // prevent duplicates events due to recompose happening too fast
        //if(challengesScrollPosition >= ((page.value * PAGE_SIZE)-1)){
            getCompletedChallenges(page.value)
            page.value = page.value+1
            Log.d("Codewars", "nextPage: triggered ${page.value}")
        //}
    }

    // Append challenges to the current list
    private fun appendChallenges(challenges: List<CompletedChallenge>){
        val current = ArrayList(completedChallenges.value)
        current.addAll(challenges)
        completedChallenges.value = current
    }

    fun onChangeChallengesScrollPosition(position: Int){
        challengesScrollPosition = position
    }
}