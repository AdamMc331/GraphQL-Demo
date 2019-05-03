package com.adammcneilly.graphqldemo.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.adammcneilly.graphqldemo.DemoApp
import com.adammcneilly.graphqldemo.R
import com.adammcneilly.graphqldemo.data.ApolloGitHubService
import com.adammcneilly.graphqldemo.databinding.ActivityRepositoryDetailBinding

class RepositoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRepositoryDetailBinding
    private lateinit var viewModel: RepositoryDetailActivityViewModel

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val client = (application as DemoApp).apolloClient
            val dataRepository = ApolloGitHubService(client)
            val repoName = intent.getStringExtra(REPOSITORY_NAME)

            @Suppress("UNCHECKED_CAST")
            return RepositoryDetailActivityViewModel(dataRepository, repoName) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repository_detail)
        setupToolbar()
        setupViewModel()
        viewModel.fetchRepositoryDetail()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RepositoryDetailActivityViewModel::class.java)
        binding.viewModel = viewModel
    }

    companion object {
        const val REPOSITORY_NAME = "repositoryName"
    }
}
