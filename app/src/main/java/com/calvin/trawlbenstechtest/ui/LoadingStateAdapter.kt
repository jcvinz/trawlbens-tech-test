package com.calvin.trawlbenstechtest.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.calvin.trawlbenstechtest.databinding.FooterLoadingBinding

class LoadingStateAdapter : LoadStateAdapter<LoadingStateAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: FooterLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FooterLoadingBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
}