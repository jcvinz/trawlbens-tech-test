package com.calvin.trawlbenstechtest.ui.detail

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.calvin.trawlbenstechtest.databinding.ActivityAnimeDetailBinding
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeDetailUiModel
import com.calvin.trawlbenstechtest.domain.util.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnimeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimeDetailBinding
    private val viewModel by viewModels<AnimeDetailViewModel>()

    private lateinit var tempData: AnimeDetailUiModel

    private var id: Int? = null

    companion object {
        const val ID = "ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getIntExtra(ID, -1)
        id?.let {
            if (id != -1) {
                viewModel.getAnimeDetail(it)
                viewModel.isFavoriteAnime(it)
            }
        }

        setupObserver()
        setupListener()
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.animeDetailUiModel.collectLatest { state ->
                        when (state) {
                            is ViewState.Loading -> {}
                            is ViewState.Success -> {
                                setDetailAnimeData(state.data)
                                tempData = state.data
                            }

                            is ViewState.Error -> {}
                        }
                    }
                }

                launch {
                    viewModel.isFavoriteViewState.collectLatest { state ->
                        when (state) {
                            is ViewState.Loading -> {}
                            is ViewState.Success -> {
                                state.data?.let { binding.cbSaveIcon.isChecked = it }
                            }

                            is ViewState.Error -> {}
                        }
                    }
                }

                launch {
                    viewModel.favoriteViewState.collectLatest { state ->
                        when (state) {
                            is ViewState.Loading -> {}
                            is ViewState.Success -> {
                                Toast.makeText(
                                    this@AnimeDetailActivity,
                                    state.data,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is ViewState.Error -> {
                                Toast.makeText(
                                    this@AnimeDetailActivity,
                                    state.exception.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                                println("MASUK SINI BABI ${state.exception.message}")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupListener() = with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        cbSaveIcon.setOnClickListener {
            if (!cbSaveIcon.isChecked) {
                println("MASUK SINI DELETE")
                id?.let { viewModel.deleteFromFavorite(it) }
            } else {
                println("MASUK SINI ADD")
                id?.let { viewModel.addToFavorite(it, tempData.title, tempData.coverImage, true) }
            }
        }
    }

    private fun setDetailAnimeData(data: AnimeDetailUiModel) = with(binding) {
        tvAnimeTitle.text = data.title

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvAnimeDesc.text = Html.fromHtml(data.description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            tvAnimeDesc.text = Html.fromHtml(data.description)
        }

        Glide.with(applicationContext)
            .load(data.coverImage)
            .centerCrop()
            .into(ivCoverImage)

        Glide.with(applicationContext)
            .load(data.bannerImage)
            .centerCrop()
            .into(ivBanner)
    }
}