package com.example.codewarschallenge.db.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "completedChallenges")
@Parcelize
data class CompletedChallenge(
    @PrimaryKey val id : String,
    val name: String,
    val slug: String,
    val completedDate : String,
    val completedLanguages : ArrayList<String>
) : Parcelable
