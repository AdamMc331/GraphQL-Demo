package com.adammcneilly.graphqldemo.repository

sealed class RepositoryActivityState {
    object Loading : RepositoryActivityState()
    class Loaded(val repositories: List<Repository>) : RepositoryActivityState()
    class Error(val error: Throwable?) : RepositoryActivityState()
}