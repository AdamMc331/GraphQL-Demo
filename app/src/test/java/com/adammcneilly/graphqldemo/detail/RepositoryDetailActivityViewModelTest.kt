package com.adammcneilly.graphqldemo.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adammcneilly.graphqldemo.data.GitHubRepository
import com.adammcneilly.graphqldemo.testObserver
import com.adammcneilly.graphqldemo.util.DispatcherProvider
import com.adammcneilly.graphqldemo.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

class RepositoryDetailActivityViewModelTest {

    @JvmField
    @Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val mockRepository = Mockito.mock(GitHubRepository::class.java)
    private val testDispatcher = DispatcherProvider(IO = Dispatchers.Unconfined, Main = Dispatchers.Unconfined)

    @Test
    fun loadData() {
        runBlocking {
            val testResponse = RepositoryDetail(id = "test")
            whenever(mockRepository.getRepositoryDetailAsync(anyString())).thenReturn(testResponse)
            val viewModel =
                RepositoryDetailActivityViewModel(mockRepository, "", testDispatcher)
            val observer = viewModel.state.testObserver()
            viewModel.fetchRepositoryDetail()

            assertFalse(viewModel.showLoading)
            assertTrue(viewModel.showData)
            assertFalse(viewModel.showError)

            assertTrue(observer.observedValues.first() is RepositoryDetailActivityState.Loading)
            assertTrue(observer.observedValues[1] is RepositoryDetailActivityState.Loaded)

            val detail = (observer.observedValues[1] as RepositoryDetailActivityState.Loaded).repositoryDetail
            assertEquals(testResponse, detail)
        }
    }

    @Test
    fun displayData() {
        val testName = "Test Name"
        val testDescription = "Test Description"
        val testIssueCount = 1
        val testPRCount = 2
        val testStarCount = 3
        val testForkCount = 4
        val testReleaseCount = 5

        runBlocking {
            val testResponse = RepositoryDetail(
                name = testName,
                description = testDescription,
                issuesCount = testIssueCount,
                pullRequestCount = testPRCount,
                stargazersCount = testStarCount,
                forkCount = testForkCount,
                releaseCount = testReleaseCount
            )

            whenever(mockRepository.getRepositoryDetailAsync(anyString())).thenReturn(testResponse)
            val viewModel =
                RepositoryDetailActivityViewModel(mockRepository, "", testDispatcher)
            viewModel.fetchRepositoryDetail()

            assertEquals(testName, viewModel.repositoryName)
            assertEquals(testDescription, viewModel.repositoryDescription)
            assertEquals("$testIssueCount Issues", viewModel.repositoryIssues)
            assertEquals("$testPRCount Pull Requests", viewModel.repositoryPullRequests)
            assertEquals("$testStarCount Stars", viewModel.repositoryStars)
            assertEquals("$testForkCount Forks", viewModel.repositoryForks)
            assertEquals("$testReleaseCount Releases", viewModel.repositoryReleases)
        }
    }

    @Test
    fun loadError() {
        runBlocking {
            val testThrowable = IllegalArgumentException("Whoops")
            whenever(mockRepository.getRepositoryDetailAsync(anyString())).thenThrow(testThrowable)
            val viewModel =
                RepositoryDetailActivityViewModel(mockRepository, "", testDispatcher)
            val observer = viewModel.state.testObserver()
            viewModel.fetchRepositoryDetail()

            assertFalse(viewModel.showLoading)
            assertFalse(viewModel.showData)
            assertTrue(viewModel.showError)

            assertTrue(observer.observedValues.first() is RepositoryDetailActivityState.Loading)
            assertTrue(observer.observedValues[1] is RepositoryDetailActivityState.Error)

            val error = (observer.observedValues[1] as RepositoryDetailActivityState.Error).error
            assertEquals(testThrowable, error)
        }
    }
}