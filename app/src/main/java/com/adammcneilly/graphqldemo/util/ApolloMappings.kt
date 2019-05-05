package com.adammcneilly.graphqldemo.util

import com.adammcneilly.graphqldemo.detail.RepositoryDetail
import com.adammcneilly.graphqldemo.graphql.api.fragment.RepositoryDetailFragment
import com.adammcneilly.graphqldemo.graphql.api.fragment.RepositoryFragment
import com.adammcneilly.graphqldemo.repository.Repository

fun RepositoryFragment.toRepository(): Repository {
    return Repository(
        id = this.id(),
        name = this.name(),
        description = this.description()
    )
}

fun RepositoryDetailFragment.toRepositoryDetail(): RepositoryDetail {
    return RepositoryDetail(
        id = this.id(),
        name = this.name(),
        description = this.description(),
        issuesCount = this.issues().totalCount(),
        pullRequestCount = this.pullRequests().totalCount(),
        stargazersCount = this.stargazers().totalCount(),
        forkCount = this.forkCount(),
        releaseCount = this.releases().totalCount()
    )
}
