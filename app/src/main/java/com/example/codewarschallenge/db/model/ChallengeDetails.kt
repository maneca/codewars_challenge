package com.example.codewarschallenge.db.model

data class ChallengeDetails(
    val id: String = "",
    val name: String = "",
    val slug: String = "",
    val url: String = "",
    val category: String = "",
    val description: String = "",
    val languages: List<String> = listOf(),
    val tags: List<String> = listOf(),
    val rank: Rank? = null,
    val createdBy: User? = null,
    val approvedBy: User? = null,
    val totalAttempts: Int = 0,
    val totalCompleted: Int = 0,
    val totalStars: Int = 0,
    val voteScore: Int = 0,
    val publishedAt: String = "",
    val approvedAt: String = "",
    val contributorsWanted: Boolean = false,
    val unresolved: UnresolvedIssues? = null,
)

data class Rank(
    val id: Int,
    val name: String,
    val color: String
)

data class User(
    val username: String,
    val url: String
)

data class UnresolvedIssues(
    val issues: Int,
    val suggestions: Int
)
