package com.adammcneilly.graphqldemo.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adammcneilly.graphqldemo.data.GitHubRepository
import com.adammcneilly.graphqldemo.repository.Repository
import com.adammcneilly.graphqldemo.repository.RepositoryActivityState
import com.adammcneilly.graphqldemo.repository.RepositoryActivityViewModel
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
import org.mockito.Mockito.mock
import java.lang.IllegalArgumentException

class RepositoryActivityViewModelTest {

    @JvmField
    @Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val mockRepository = mock(GitHubRepository::class.java)
    private val testDispatcher = DispatcherProvider(IO = Dispatchers.Unconfined, Main = Dispatchers.Unconfined)

    @Test
    fun loadData() {
        runBlocking {
            val testRepos = listOf(Repository(id = "test"))
            whenever(mockRepository.getRepositoriesAsync()).thenReturn(testRepos)
            val viewModel =
                RepositoryActivityViewModel(mockRepository, testDispatcher)
            val observer = viewModel.state.testObserver()
            viewModel.fetchRepositories()

            assertFalse(viewModel.showLoading)
            assertTrue(viewModel.showData)
            assertFalse(viewModel.showError)

            assertTrue(observer.observedValues.first() is RepositoryActivityState.Loading)
            assertTrue(observer.observedValues[1] is RepositoryActivityState.Loaded)
            assertEquals(testRepos, (observer.observedValues[1] as RepositoryActivityState.Loaded).repositories)
        }
    }

    @Test
    fun loadError() {
        runBlocking {
            val testThrowable = IllegalArgumentException("Whoops")
            whenever(mockRepository.getRepositoriesAsync()).thenThrow(testThrowable)
            val viewModel =
                RepositoryActivityViewModel(mockRepository, testDispatcher)
            val observer = viewModel.state.testObserver()
            viewModel.fetchRepositories()

            assertFalse(viewModel.showLoading)
            assertFalse(viewModel.showData)
            assertTrue(viewModel.showError)

            assertTrue(observer.observedValues.first() is RepositoryActivityState.Loading)
            assertTrue(observer.observedValues[1] is RepositoryActivityState.Error)
            assertEquals(testThrowable, (observer.observedValues[1] as RepositoryActivityState.Error).error)
        }
    }
}