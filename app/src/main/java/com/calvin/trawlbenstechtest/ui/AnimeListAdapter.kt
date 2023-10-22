package com.calvin.trawlbenstechtest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.calvin.trawlbenstechtest.databinding.ItemAnimeCardBinding
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeCardUiModel
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeDiffUtil

class AnimeListAdapter(private val onItemClick: (Int) -> Unit) :
    PagingDataAdapter<AnimeCardUiModel, AnimeListAdapter.ViewHolder>(
        AnimeDiffUtil
    ) {

    inner class ViewHolder(private val binding: ItemAnimeCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: AnimeCardUiModel) = with(binding) {
            Glide.with(this.root.context)
                .load(data.coverImage)
                .centerCrop()
                .into(this.ivCoverImage)

            this.tvAnimeTitle.text = data.title

            this.root.setOnClickListener {
                onItemClick(data.id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemAnimeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}