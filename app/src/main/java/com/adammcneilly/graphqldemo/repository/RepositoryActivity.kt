package com.adammcneilly.graphqldemo.repository

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.adammcneilly.graphqldemo.DemoApp
import com.adammcneilly.graphqldemo.R
import com.adammcneilly.graphqldemo.data.ApolloGitHubService
import com.adammcneilly.graphqldemo.databinding.ActivityRepositoryBinding
import com.adammcneilly.graphqldemo.detail.RepositoryDetailActivity

class RepositoryActivity : AppCompatActivity() {
    private val adapter = RepositoryAdapter(this::showRepositoryDetail)
    private lateinit var binding: ActivityRepositoryBinding
    private lateinit var viewModel: RepositoryActivityViewModel

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val client = (application as DemoApp).apolloClient
            val repository = ApolloGitHubService(client)

            @Suppress("UNCHECKED_CAST")
            return RepositoryActivityViewModel(repository) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repository)
        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        setupViewModel()
        viewModel.fetchRepositories()
    }

    private fun setupRecyclerView() {
        binding.repositoryList.adapter = adapter
        binding.repositoryList.layoutManager = LinearLayoutManager(this)
        binding.repositoryList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RepositoryActivityViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is RepositoryActivityState.Loaded -> adapter.repositories = state.repositories
            }
        })
    }

    private fun showRepositoryDetail(repository: Repository) {
        val intent = Intent(this, RepositoryDetailActivity::class.java)
        intent.putExtra(RepositoryDetailActivity.REPOSITORY_NAME, repository.name)

        startActivity(intent)
    }
}
