package com.example.codewarschallenge.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.codewarschallenge.db.model.CompletedChallenge

@Dao
interface ChallengesDao {

    @Query("SELECT * FROM completedChallenges")
    fun getCompletedChallenges() : List<CompletedChallenge>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCompletedChallenges(challenges : List<CompletedChallenge>)
}