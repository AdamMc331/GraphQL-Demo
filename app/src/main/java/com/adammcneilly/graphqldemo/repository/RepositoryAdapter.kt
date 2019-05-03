package com.adammcneilly.graphqldemo.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adammcneilly.graphqldemo.databinding.ListItemRepositoryBinding

class RepositoryAdapter(
    private val repoClickListener: (Repository) -> Unit
) : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {
    var repositories: List<Repository> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ListItemRepositoryBinding.inflate(inflater, parent, false)
        return RepositoryViewHolder(binding, repoClickListener)
    }

    override fun getItemCount(): Int {
        return repositories.size
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bindRepository(repositories[position])
    }

    class RepositoryViewHolder(
        private val binding: ListItemRepositoryBinding,
        repoClickListener: (Repository) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = RepositoryViewModel()

        init {
            binding.viewModel = viewModel
            binding.root.setOnClickListener {
                viewModel.repository?.let(repoClickListener::invoke)
            }
        }

        fun bindRepository(repository: Repository) {
            viewModel.repository = repository
            binding.executePendingBindings()
        }
    }
}