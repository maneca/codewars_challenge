package com.example.codewarschallenge.api

import com.example.codewarschallenge.db.model.CompletedChallenge

data class ApiResponse(
    val totalPages: Int,
    val totalItems: Int,
    val data: List<CompletedChallenge>
)
