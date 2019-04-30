package com.adammcneilly.graphqldemo.data

import com.adammcneilly.graphqldemo.repository.Repository

interface GitHubRepository {
    suspend fun getRepositoriesAsync(): List<Repository>
}