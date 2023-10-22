package com.calvin.trawlbenstechtest.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.calvin.trawlbenstechtest.databinding.FragmentFavoriteBinding
import com.calvin.trawlbenstechtest.domain.util.ViewState
import com.calvin.trawlbenstechtest.ui.FavoriteAnimeListAdapter
import com.calvin.trawlbenstechtest.ui.detail.AnimeDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavoriteViewModel>()
    private lateinit var favoriteAnimeListAdapter: FavoriteAnimeListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteAnimeList()
        setupRecyclerView()
        setupObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteAnimeList()
    }

    private fun setupRecyclerView() = with(binding.rvAnimeList) {
        favoriteAnimeListAdapter = FavoriteAnimeListAdapter {
            val intent = Intent(context, AnimeDetailActivity::class.java)
            intent.putExtra(AnimeDetailActivity.ID, it)
            startActivity(intent)
        }

        this.apply {
            adapter = favoriteAnimeListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

    }

    private fun setupObserver() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.favoriteAnimeListViewState.collectLatest { state ->
            when (state) {
                is ViewState.Loading -> {}
                is ViewState.Success -> {
                    if(state.data.isNotEmpty()) {
                        binding.rvAnimeList.visibility = View.VISIBLE
                        binding.tvEmptyState.visibility = View.GONE
                        favoriteAnimeListAdapter.setData(state.data)
                    } else {
                        binding.rvAnimeList.visibility = View.GONE
                        binding.tvEmptyState.visibility = View.VISIBLE
                    }
                }

                is ViewState.Error -> {}
            }
        }
    }

}