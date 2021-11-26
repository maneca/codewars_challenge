package com.example.codewarschallenge.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.codewarschallenge.db.ChallengesDatabase
import com.example.codewarschallenge.utils.challenge
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ChallengesDaoTest {

    private lateinit var database: ChallengesDatabase
    private lateinit var dao: ChallengesDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ChallengesDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.challengesDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertCompletedChallenge() {
        dao.addCompletedChallenges(listOf(challenge))

        val challenges = dao.getCompletedChallenges()
        Assert.assertEquals(challenges.size, 1)
        Assert.assertTrue(challenges.any { it.name == "challenge" })
    }
}