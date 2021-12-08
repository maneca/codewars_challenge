package com.example.codewarschallenge.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.codewarschallenge.api.ApiResponse
import com.example.codewarschallenge.api.ChallengesApi
import com.example.codewarschallenge.db.dao.ChallengesDao
import com.example.codewarschallenge.db.model.ChallengeDetails
import com.example.codewarschallenge.utils.AppResult
import com.example.codewarschallenge.utils.challenge
import com.example.codewarschallenge.utils.generateRandomString
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
@SmallTest
class ChallengesRepositoryTest {

    @MockK
    private lateinit var mockApi: ChallengesApi

    @MockK(relaxUnitFun = true)
    private lateinit var mockDao: ChallengesDao

    @MockK
    private lateinit var mockContext: Context

    private lateinit var context: Context

    private lateinit var repository: ChallengesRepository

    private val challengeDetails = ChallengeDetails(
        id = generateRandomString(),
        name = "challenge 1",
        slug = generateRandomString(),
        languages = listOf("java", "kotlin", "lisp")
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun getCompleteChallengesFromNetwork() = runBlocking {
        Assert.assertNotNull(mockDao)
        Assert.assertNotNull(mockApi)
        val apiResponse = ApiResponse(totalItems = 1, totalPages = 1, data = listOf(challenge))
        coEvery { mockDao.getCompletedChallenges() } coAnswers { listOf() }
        coEvery { mockApi.getCompletedChallenges(0) } coAnswers { Response.success(apiResponse) }

        repository = ChallengesRepositoryImp(mockApi, mockDao)
        val result =
            repository.getCompletedChallenges(0) as AppResult.Success<ApiResponse?>

        Assert.assertEquals(1, result.successData!!.data.size)
        Assert.assertEquals(challenge.name, result.successData!!.data[0].name)
    }

    @Test
    fun getCompleteChallengesFromDatabase() = runBlocking {
        Assert.assertNotNull(mockDao)
        coEvery { mockDao.getCompletedChallenges() } coAnswers { listOf(challenge) }

        repository = ChallengesRepositoryImp(mockApi, mockDao)
        val result =
            repository.getCompletedChallenges(0) as AppResult.Success<ApiResponse?>

        Assert.assertEquals(1, result.successData!!.data.size)
        Assert.assertEquals(challenge.name, result.successData!!.data[0].name)

    }

    @Test
    fun getChallengeDetails() = runBlocking {
        Assert.assertNotNull(mockApi)
        coEvery { mockApi.getChallengeDetails("challenge") } coAnswers {
            Response.success(
                challengeDetails
            )
        }

        repository = ChallengesRepositoryImp(mockApi, mockDao)
        val result =
            repository.getChallengeDetails("challenge") as AppResult.Success<ChallengeDetails>

        Assert.assertEquals(challengeDetails.name, result.successData.name)
        Assert.assertEquals(3, result.successData.languages.size)
    }
}