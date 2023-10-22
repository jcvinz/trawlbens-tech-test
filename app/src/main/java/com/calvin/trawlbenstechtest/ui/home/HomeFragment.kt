package com.calvin.trawlbenstechtest.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.calvin.trawlbenstechtest.databinding.FragmentHomeBinding
import com.calvin.trawlbenstechtest.domain.util.ViewState
import com.calvin.trawlbenstechtest.ui.AnimeListAdapter
import com.calvin.trawlbenstechtest.ui.LoadingStateAdapter
import com.calvin.trawlbenstechtest.ui.custom.FilterDialog
import com.calvin.trawlbenstechtest.ui.detail.AnimeDetailActivity
import com.calvin.type.MediaSeason
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val selectedGenre = mutableListOf<String>()
    private var selectedSeason = ""
    private var selectedSeasonType: MediaSeason? = null

    private lateinit var animeListAdapter: AnimeListAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAnimeCardList(null, null, null)

        binding.btnFilter.setOnClickListener {
            FilterDialog(selectedGenre, selectedSeason, onSubmit = { genreResult, season ->
                selectedGenre.clear()
                selectedGenre.addAll(genreResult)
                when (season) {
                    "WINTER" -> selectedSeasonType = MediaSeason.WINTER
                    "SPRING" -> selectedSeasonType = MediaSeason.SPRING
                    "SUMMER" -> selectedSeasonType = MediaSeason.SUMMER
                    "FALL" -> selectedSeasonType = MediaSeason.FALL
                }
                selectedSeason = season
                viewModel.getAnimeCardList(selectedGenre, selectedSeasonType, null)
            }).show(requireActivity().supportFragmentManager, null)
        }

        setupRecyclerView()
        setupSearchListener()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAnimeCardList(null, null, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        selectedGenre.clear()
        selectedSeason = ""
        selectedSeasonType = null
        _binding = null
    }

    private fun setupRecyclerView() = with(binding.rvAnimeList) {
        animeListAdapter = AnimeListAdapter {
            val intent = Intent(context, AnimeDetailActivity::class.java)
            intent.putExtra(AnimeDetailActivity.ID, it)
            startActivity(intent)
        }

        animeListAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.Loading) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.rvAnimeList.visibility = View.GONE
            }

            if (loadState.source.refresh is LoadState.NotLoading) {
                binding.tvEmptyState.visibility = View.GONE
                binding.rvAnimeList.visibility = View.VISIBLE
            }

            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                if (animeListAdapter.itemCount < 1) {
                    this.visibility = View.GONE
                    binding.tvEmptyState.visibility = View.VISIBLE
                } else {
                    this.visibility = View.VISIBLE
                    binding.tvEmptyState.visibility = View.GONE
                }
            }
        }

        this.apply {
            adapter = animeListAdapter.withLoadStateFooter(LoadingStateAdapter())
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.animeListViewState.collectLatest { state ->
                when (state) {
                    is ViewState.Loading -> {}
                    is ViewState.Success -> {
                        animeListAdapter.submitData(state.data)
                    }

                    is ViewState.Error -> {}
                }
            }
        }

    }

    private fun setupSearchListener() = with(binding) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if (!query.isNullOrEmpty()) {
                    if (selectedGenre.isNotEmpty())
                        viewModel.getAnimeCardList(selectedGenre, selectedSeasonType, query)
                    else
                        viewModel.getAnimeCardList(null, selectedSeasonType, query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    if (selectedGenre.isNotEmpty())
                        viewModel.getAnimeCardList(selectedGenre, selectedSeasonType, null)
                    else
                        viewModel.getAnimeCardList(null, selectedSeasonType, null)
                }
                return false
            }
        })
    }

}