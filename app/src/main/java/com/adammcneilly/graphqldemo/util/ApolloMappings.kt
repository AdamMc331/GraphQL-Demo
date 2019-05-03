package com.adammcneilly.graphqldemo.util

import com.adammcneilly.graphqldemo.graphql.api.fragment.RepositoryFragment
import com.adammcneilly.graphqldemo.repository.Repository

fun RepositoryFragment.toRepository(): Repository {
    return Repository(
        id = this.id(),
        name = this.name(),
        description = this.description()
    )
}