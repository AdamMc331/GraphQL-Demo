package com.adammcneilly.graphqldemo.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adammcneilly.graphqldemo.data.GitHubRepository
import com.adammcneilly.graphqldemo.util.BaseObservableViewModel
import com.adammcneilly.graphqldemo.util.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepositoryDetailActivityViewModel(
    private val dataRepository: GitHubRepository,
    private val repoName: String,
    private val dispatcherProvider: DispatcherProvider = DispatcherProvider()
) : BaseObservableViewModel() {
    private val _state = MutableLiveData<RepositoryDetailActivityState>()
    val state: LiveData<RepositoryDetailActivityState> = _state

    val showLoading: Boolean
        get() = state.value == null || state.value is RepositoryDetailActivityState.Loading

    val showData: Boolean
        get() = state.value is RepositoryDetailActivityState.Loaded

    val showError: Boolean
        get() = state.value is RepositoryDetailActivityState.Error

    val repositoryName: String
        get() = (state.value as? RepositoryDetailActivityState.Loaded)?.repositoryDetail?.name.orEmpty()

    val repositoryDescription: String
        get() = (state.value as? RepositoryDetailActivityState.Loaded)?.repositoryDetail?.description.orEmpty()

    val repositoryIssues: String
        get() {
            val issueCount = (state.value as? RepositoryDetailActivityState.Loaded)?.repositoryDetail?.issuesCount ?: 0
            return "$issueCount Issues"
        }

    val repositoryPullRequests: String
        get() {
            val pullRequestCount =
                (state.value as? RepositoryDetailActivityState.Loaded)?.repositoryDetail?.pullRequestCount ?: 0
            return "$pullRequestCount Pull Requests"
        }

    val repositoryStars: String
        get() {
            val starsCount =
                (state.value as? RepositoryDetailActivityState.Loaded)?.repositoryDetail?.stargazersCount ?: 0
            return "$starsCount Stars"
        }

    val repositoryForks: String
        get() {
            val forksCount = (state.value as? RepositoryDetailActivityState.Loaded)?.repositoryDetail?.forkCount ?: 0
            return "$forksCount Forks"
        }

    val repositoryReleases: String
        get() {
            val releaseCount =
                (state.value as? RepositoryDetailActivityState.Loaded)?.repositoryDetail?.releaseCount ?: 0
            return "$releaseCount Releases"
        }

    private var job: Job? = null

    fun fetchRepositoryDetail() {
        if (showData) return

        job = CoroutineScope(dispatcherProvider.IO).launch {
            postStateToMain(RepositoryDetailActivityState.Loading)

            @Suppress("TooGenericExceptionCaught")
            val newState = try {
                val response = dataRepository.getRepositoryDetailAsync(repoName)
                RepositoryDetailActivityState.Loaded(response)
            } catch (e: Throwable) {
                RepositoryDetailActivityState.Error(e)
            }

            postStateToMain(newState)
        }
    }

    /**
     * Updates to the main thread context and sets a new state.
     */
    private suspend fun postStateToMain(newState: RepositoryDetailActivityState) {
        withContext(dispatcherProvider.Main) {
            setState(newState)
        }
    }

    /**
     * Updates the state of the viewmodel. This should be called on the main thread.
     */
    private fun setState(newState: RepositoryDetailActivityState) {
        _state.value = newState
        notifyChange()
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}