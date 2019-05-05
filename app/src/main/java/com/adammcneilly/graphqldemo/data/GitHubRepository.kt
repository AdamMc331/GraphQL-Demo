package com.adammcneilly.graphqldemo.data

import com.adammcneilly.graphqldemo.detail.RepositoryDetail
import com.adammcneilly.graphqldemo.repository.Repository

interface GitHubRepository {
    suspend fun getRepositoriesAsync(): List<Repository>
    suspend fun getRepositoryDetailAsync(repositoryName: String): RepositoryDetail
}