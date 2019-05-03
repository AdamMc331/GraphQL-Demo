package com.adammcneilly.graphqldemo.data

import com.adammcneilly.graphqldemo.graphql.api.GithubRepositoriesQuery
import com.adammcneilly.graphqldemo.graphql.api.type.OrderDirection
import com.adammcneilly.graphqldemo.graphql.api.type.RepositoryOrderField
import com.adammcneilly.graphqldemo.repository.Repository
import com.adammcneilly.graphqldemo.util.toRepository
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ApolloGitHubService(
    private val apolloClient: ApolloClient
) : GitHubRepository {
    override suspend fun getRepositoriesAsync(): List<Repository> {
        return suspendCoroutine { continuation ->
            val query = GithubRepositoriesQuery
                .builder()
                .repositoriesCount(REPO_FETCH_COUNT)
                .orderBy(RepositoryOrderField.UPDATED_AT)
                .orderDirection(OrderDirection.DESC)
                .build()

            val call = apolloClient.query(query)

            call.enqueue(object : ApolloCall.Callback<GithubRepositoriesQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(response: Response<GithubRepositoriesQuery.Data>) {
                    val repos = response.data()?.viewer()?.repositories()?.nodes()?.map {
                        it.fragments().repositoryFragment().toRepository()
                    }.orEmpty()

                    continuation.resume(repos)
                }
            })
        }
    }

    companion object {
        private const val REPO_FETCH_COUNT = 50
    }
}