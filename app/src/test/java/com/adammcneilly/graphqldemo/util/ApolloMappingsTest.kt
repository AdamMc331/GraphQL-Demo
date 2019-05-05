package com.adammcneilly.graphqldemo.util

import com.adammcneilly.graphqldemo.graphql.api.fragment.RepositoryDetailFragment
import com.adammcneilly.graphqldemo.graphql.api.fragment.RepositoryFragment
import org.junit.Assert.assertEquals
import org.junit.Test

class ApolloMappingsTest {

    @Test
    fun repositoryFragmentToRepository() {
        val testId = "testID"
        val testName = "testName"
        val testDescription = "testDescription"

        val repositoryFragment = RepositoryFragment
            .builder()
            .__typename("")
            .id(testId)
            .name(testName)
            .description(testDescription)
            .build()

        val repository = repositoryFragment.toRepository()

        assertEquals(testId, repository.id)
        assertEquals(testName, repository.name)
        assertEquals(testDescription, repository.description)
    }

    @Test
    fun repositoryDetailFragmentToRepositoryDetail() {
        val testId = "testID"
        val testName = "testName"
        val testDescription = "testDescription"
        val testIssues = 1
        val testPullRequests = 2
        val testStargazers = 3
        val testForks = 4
        val testReleases = 5

        val issues = RepositoryDetailFragment.Issues
            .builder()
            .__typename("")
            .totalCount(testIssues)
            .build()

        val pullRequests = RepositoryDetailFragment.PullRequests
            .builder()
            .__typename("")
            .totalCount(testPullRequests)
            .build()

        val stargazers = RepositoryDetailFragment.Stargazers
            .builder()
            .__typename("")
            .totalCount(testStargazers)
            .build()

        val releases = RepositoryDetailFragment.Releases
            .builder()
            .__typename("")
            .totalCount(testReleases)
            .build()

        val repositoryDetailFragment = RepositoryDetailFragment
            .builder()
            .__typename("")
            .id(testId)
            .name(testName)
            .description(testDescription)
            .issues(issues)
            .pullRequests(pullRequests)
            .stargazers(stargazers)
            .forkCount(testForks)
            .releases(releases)
            .build()

        val repositoryDetail = repositoryDetailFragment.toRepositoryDetail()

        assertEquals(testId, repositoryDetail.id)
        assertEquals(testName, repositoryDetail.name)
        assertEquals(testDescription, repositoryDetail.description)
        assertEquals(testIssues, repositoryDetail.issuesCount)
        assertEquals(testPullRequests, repositoryDetail.pullRequestCount)
        assertEquals(testStargazers, repositoryDetail.stargazersCount)
        assertEquals(testForks, repositoryDetail.forkCount)
        assertEquals(testReleases, repositoryDetail.releaseCount)
    }
}