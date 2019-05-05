package com.adammcneilly.graphqldemo.detail

data class RepositoryDetail(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val issuesCount: Int? = null,
    val pullRequestCount: Int? = null,
    val stargazersCount: Int? = null,
    val forkCount: Int? = null,
    val releaseCount: Int? = null
)