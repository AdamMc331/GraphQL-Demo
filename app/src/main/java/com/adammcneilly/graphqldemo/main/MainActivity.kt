package com.adammcneilly.graphqldemo.main

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
import com.adammcneilly.graphqldemo.databinding.ActivityMainBinding
import com.adammcneilly.graphqldemo.repository.RepositoryAdapter

class MainActivity : AppCompatActivity() {
    private val adapter = RepositoryAdapter()
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val client = (application as DemoApp).apolloClient
            val repository = ApolloGitHubService(client)

            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(repository) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is MainActivityState.Loaded -> adapter.repositories = state.repositories
            }
        })
    }
}
