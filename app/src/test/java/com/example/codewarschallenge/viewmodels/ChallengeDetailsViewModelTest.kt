package com.example.codewarschallenge.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.codewarschallenge.db.model.ChallengeDetails
import com.example.codewarschallenge.repository.ChallengesRepository
import com.example.codewarschallenge.utils.AppResult
import com.example.codewarschallenge.utils.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
class ChallengeDetailsViewModelTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var mockRepo: ChallengesRepository

    private lateinit var viewModel: ChallengeDetailsViewModel

    private val challengeDetails = ChallengeDetails(
        id = "challenge1",
        name = "challenge 1",
        slug = "challenge 1",
        languages = listOf("java", "kotlin", "lisp")
    )

    @After
    fun stopKoinAfterTest() = stopKoin()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `get challenge details, exception`() = runBlockingTest {
        assertNotNull(mockRepo)
        coEvery { mockRepo.getChallengeDetails(any()) } coAnswers { AppResult.Error(Exception("")) }
        viewModel = ChallengeDetailsViewModel("challengeId", mockRepo)

        assertEquals("", viewModel.showError.value)
    }

    @Test
    fun `get challenge details, successful`() = runBlockingTest {
        assertNotNull(mockRepo)
        coEvery { mockRepo.getChallengeDetails("challenge1") } coAnswers {
            AppResult.Success(
                challengeDetails
            )
        }
        viewModel = ChallengeDetailsViewModel("challenge1", mockRepo)

        assertEquals(challengeDetails.id, viewModel.challengeDetails.value.id)
        assertEquals(challengeDetails.name, viewModel.challengeDetails.value.name)
    }
}