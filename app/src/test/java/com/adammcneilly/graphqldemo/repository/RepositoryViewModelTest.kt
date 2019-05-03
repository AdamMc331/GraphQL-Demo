package com.adammcneilly.graphqldemo.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryViewModelTest {

    @Test
    fun getName() {
        val defaultViewModel = RepositoryViewModel()
        assertEquals("", defaultViewModel.name)

        val testName = "Adam"
        val viewModel = RepositoryViewModel().apply { repository = Repository(name = testName) }
        assertEquals(testName, viewModel.name)
    }

    @Test
    fun getDescription() {
        val defaultViewModel = RepositoryViewModel()
        assertEquals("", defaultViewModel.description)

        val testDescription = "Adam"
        val viewModel = RepositoryViewModel().apply { repository = Repository(description = testDescription) }
        assertEquals(testDescription, viewModel.description)
    }
}