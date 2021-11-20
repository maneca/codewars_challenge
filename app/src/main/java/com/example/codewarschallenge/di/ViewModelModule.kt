package com.example.codewarschallenge.di

import com.example.codewarschallenge.viewmodels.CompletedChallengesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{
        CompletedChallengesViewModel(repository = get())
    }
}