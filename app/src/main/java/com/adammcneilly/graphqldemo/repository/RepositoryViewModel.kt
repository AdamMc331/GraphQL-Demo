package com.adammcneilly.graphqldemo.repository

import androidx.databinding.BaseObservable

class RepositoryViewModel : BaseObservable() {
    var repository: Repository? = null
        set(value) {
            field = value
            notifyChange()
        }

    val name: String
        get() = repository?.name.orEmpty()

    val description: String
        get() = repository?.description.orEmpty()
}