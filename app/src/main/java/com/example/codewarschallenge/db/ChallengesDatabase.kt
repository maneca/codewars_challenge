package com.example.codewarschallenge.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.codewarschallenge.db.converters.ListConverter
import com.example.codewarschallenge.db.dao.ChallengesDao
import com.example.codewarschallenge.db.model.CompletedChallenge

@Database(
    entities = [CompletedChallenge::class],
    version = 2, exportSchema = false
)

@TypeConverters(ListConverter::class)
abstract class ChallengesDatabase : RoomDatabase(){
    abstract val challengesDao: ChallengesDao
}