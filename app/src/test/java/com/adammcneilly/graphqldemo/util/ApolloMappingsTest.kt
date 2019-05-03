package com.adammcneilly.graphqldemo.util

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
}