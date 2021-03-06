package com.example.codewarschallenge.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.codewarschallenge.api.ApiResponse
import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.repository.ChallengesRepository
import com.example.codewarschallenge.utils.AppResult
import com.example.codewarschallenge.utils.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.util.*

@ExperimentalCoroutinesApi
class CompletedChallengesViewModelTest {
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var mockRepo: ChallengesRepository

    private lateinit var viewModel: CompletedChallengesViewModel

    private val challenge = CompletedChallenge(
        id = "id",
        name = "challenge",
        slug = "challenge",
        completedLanguages = listOf("java", "kotlin", "lisp"),
        completedAt = Date.from(Instant.now()).toString()
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `get all completed challenges, throws error`() = runBlockingTest {
        assertNotNull(mockRepo)
        coEvery { mockRepo.getCompletedChallenges(0) } coAnswers { AppResult.Error(Exception("")) }
        viewModel = CompletedChallengesViewModel(mockRepo)
        val method =
            viewModel.javaClass.getDeclaredMethod("getCompletedChallenges", Int::class.java)
        method.isAccessible = true

        assertNotNull(viewModel.showError)
        assertEquals(0, viewModel.completedChallenges.size)
    }

    @Test
    fun `get all completed challenges, successful`() = runBlockingTest {

        assertNotNull(mockRepo)
        val apiResponse = ApiResponse(1, 1, listOf(challenge))
        coEvery { mockRepo.getCompletedChallenges(0) } coAnswers { AppResult.Success(apiResponse) }
        viewModel = CompletedChallengesViewModel(mockRepo)

        assertEquals(1, viewModel.completedChallenges.size)
        assertEquals(challenge.name, viewModel.completedChallenges[0].name)
    }

    @Test
    fun `get all completed challenges, reached end`() = runBlockingTest {

        assertNotNull(mockRepo)
        val apiResponse = ApiResponse(0, 1, listOf(challenge))
        coEvery { mockRepo.getCompletedChallenges(0) } coAnswers { AppResult.Success(apiResponse) }
        viewModel = CompletedChallengesViewModel(mockRepo)

        viewModel.nextPage()
        viewModel.nextPage()

        assertNotNull(viewModel.showWarning)
        assertNull(viewModel.showError)
    }

}