package com.adammcneilly.graphqldemo.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adammcneilly.graphqldemo.util.BaseObservableViewModel
import com.adammcneilly.graphqldemo.util.DispatcherProvider
import com.adammcneilly.graphqldemo.data.GitHubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(
    private val repository: GitHubRepository,
    private val dispatcherProvider: DispatcherProvider = DispatcherProvider()
) : BaseObservableViewModel() {
    private val _state = MutableLiveData<MainActivityState>()
    val state: LiveData<MainActivityState> = _state

    val showLoading: Boolean
        get() = state.value == null || state.value is MainActivityState.Loading

    val showData: Boolean
        get() = state.value is MainActivityState.Loaded

    val showError: Boolean
        get() = state.value is MainActivityState.Error

    private var job: Job? = null

    fun fetchRepositories() {
        job = CoroutineScope(dispatcherProvider.IO).launch {
            postStateToMain(MainActivityState.Loading)

            @Suppress("TooGenericExceptionCaught")
            val newState = try {
                val response = repository.getRepositoriesAsync()
                MainActivityState.Loaded(response)
            } catch (e: Throwable) {
                MainActivityState.Error(e)
            }

            postStateToMain(newState)
        }
    }

    /**
     * Updates to the main thread context and sets a new state.
     */
    private suspend fun postStateToMain(newState: MainActivityState) {
        withContext(dispatcherProvider.Main) {
            setState(newState)
        }
    }

    /**
     * Updates the state of the viewmodel. This should be called on the main thread.
     */
    private fun setState(newState: MainActivityState) {
        _state.value = newState
        notifyChange()
    }
}