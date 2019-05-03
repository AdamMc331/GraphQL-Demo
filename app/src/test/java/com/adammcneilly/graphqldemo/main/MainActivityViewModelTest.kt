package com.adammcneilly.graphqldemo.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adammcneilly.graphqldemo.data.GitHubRepository
import com.adammcneilly.graphqldemo.repository.Repository
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

class MainActivityViewModelTest {

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
            val viewModel = MainActivityViewModel(mockRepository, testDispatcher)
            val observer = viewModel.state.testObserver()
            viewModel.fetchRepositories()

            assertFalse(viewModel.showLoading)
            assertTrue(viewModel.showData)
            assertFalse(viewModel.showError)

            assertTrue(observer.observedValues.first() is MainActivityState.Loading)
            assertTrue(observer.observedValues[1] is MainActivityState.Loaded)
            assertEquals(testRepos, (observer.observedValues[1] as MainActivityState.Loaded).repositories)
        }
    }

    @Test
    fun loadError() {
        runBlocking {
            val testThrowable = IllegalArgumentException("Whoops")
            whenever(mockRepository.getRepositoriesAsync()).thenThrow(testThrowable)
            val viewModel = MainActivityViewModel(mockRepository, testDispatcher)
            val observer = viewModel.state.testObserver()
            viewModel.fetchRepositories()

            assertFalse(viewModel.showLoading)
            assertFalse(viewModel.showData)
            assertTrue(viewModel.showError)

            assertTrue(observer.observedValues.first() is MainActivityState.Loading)
            assertTrue(observer.observedValues[1] is MainActivityState.Error)
            assertEquals(testThrowable, (observer.observedValues[1] as MainActivityState.Error).error)
        }
    }
}