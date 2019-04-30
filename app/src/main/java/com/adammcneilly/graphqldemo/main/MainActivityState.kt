package com.adammcneilly.graphqldemo.main

import com.adammcneilly.graphqldemo.repository.Repository

sealed class MainActivityState {
    object Loading : MainActivityState()
    class Loaded(val repositories: List<Repository>) : MainActivityState()
    class Error(val error: Throwable?) : MainActivityState()
}