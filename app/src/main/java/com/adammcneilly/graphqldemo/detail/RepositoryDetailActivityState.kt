package com.adammcneilly.graphqldemo.detail

sealed class RepositoryDetailActivityState {
    object Loading : RepositoryDetailActivityState()
    class Loaded(val repositoryDetail: RepositoryDetail) : RepositoryDetailActivityState()
    class Error(val error: Throwable?) : RepositoryDetailActivityState()
}