package com.example.codewarschallenge.utils

import com.example.codewarschallenge.db.model.CompletedChallenge
import java.time.Instant
import java.util.*
import kotlin.random.Random

val challenge = CompletedChallenge(
    id = generateRandomString(),
    name = "challenge",
    slug = generateRandomString(),
    completedLanguages = listOf("java", "kotlin", "lisp"),
    completedAt = Date.from(Instant.now()).toString()
)

fun generateRandomString(): String {
    val STRING_LENGTH = 10
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return (1..STRING_LENGTH)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}
