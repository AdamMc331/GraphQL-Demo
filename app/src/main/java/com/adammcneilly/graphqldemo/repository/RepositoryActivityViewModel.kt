package com.adammcneilly.graphqldemo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adammcneilly.graphqldemo.util.BaseObservableViewModel
import com.adammcneilly.graphqldemo.util.DispatcherProvider
import com.adammcneilly.graphqldemo.data.GitHubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepositoryActivityViewModel(
    private val repository: GitHubRepository,
    private val dispatcherProvider: DispatcherProvider = DispatcherProvider()
) : BaseObservableViewModel() {
    private val _state = MutableLiveData<RepositoryActivityState>()
    val state: LiveData<RepositoryActivityState> = _state

    val showLoading: Boolean
        get() = state.value == null || state.value is RepositoryActivityState.Loading

    val showData: Boolean
        get() = state.value is RepositoryActivityState.Loaded

    val showError: Boolean
        get() = state.value is RepositoryActivityState.Error

    private var job: Job? = null

    fun fetchRepositories() {
        if (showData) return

        job = CoroutineScope(dispatcherProvider.IO).launch {
            postStateToMain(RepositoryActivityState.Loading)

            @Suppress("TooGenericExceptionCaught")
            val newState = try {
                val response = repository.getRepositoriesAsync()
                RepositoryActivityState.Loaded(response)
            } catch (e: Throwable) {
                RepositoryActivityState.Error(e)
            }

            postStateToMain(newState)
        }
    }

    /**
     * Updates to the main thread context and sets a new state.
     */
    private suspend fun postStateToMain(newState: RepositoryActivityState) {
        withContext(dispatcherProvider.Main) {
            setState(newState)
        }
    }

    /**
     * Updates the state of the viewmodel. This should be called on the main thread.
     */
    private fun setState(newState: RepositoryActivityState) {
        _state.value = newState
        notifyChange()
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}