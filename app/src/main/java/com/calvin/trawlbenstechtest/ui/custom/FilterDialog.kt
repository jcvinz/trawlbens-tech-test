package com.calvin.trawlbenstechtest.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.calvin.trawlbenstechtest.R
import com.calvin.trawlbenstechtest.databinding.FilterDialogBinding
import com.calvin.trawlbenstechtest.domain.util.ViewState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterDialog(
    private val selectedGenre: List<String>,
    private val selectedSeason: String,
    private val onSubmit: (genreResult: List<String>, season: String) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: FilterDialogBinding
    private val viewModel by viewModels<FilterViewModel>()

    override fun onStart() {
        super.onStart()
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGenreList()
        setupObserver()

        binding.btnSubmit.setOnClickListener {
            val genreResult = mutableListOf<String>()
            binding.chipGroup.children.iterator().forEach {
                val chip = it as Chip
                if (it.isSelected) {
                    genreResult.add(chip.text.toString())
                }
            }
            val season = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rb_winter -> "WINTER"
                R.id.rb_spring -> "SPRING"
                R.id.rb_summer -> "SUMMER"
                R.id.rb_fall -> "FALL"
                else -> ""
            }
            onSubmit.invoke(genreResult, season)
            dismiss()
        }
    }

    private fun setupObserver() = lifecycleScope.launch {
        viewModel.genreList.collectLatest { state ->
            when (state) {
                is ViewState.Loading -> {}
                is ViewState.Success -> {
                    setupFilter(state.data, selectedGenre, selectedSeason)
                }
                is ViewState.Error -> {}
            }
        }
    }

    private fun setupFilter(data: List<String>, selectedGenre: List<String>, selectedSeason: String) =
        with(binding) {
            data.forEach {
                val chip = Chip(context)
                chip.text = it
                chip.id = View.generateViewId()
                if (selectedGenre.contains(it)) {
                    chip.isSelected = true
                }
                chip.setOnClickListener {
                    chip.isSelected = !chip.isSelected
                }
                chipGroup.addView(chip)
            }

            when(selectedSeason) {
                "WINTER" -> rbWinter.isChecked = true
                "SPRING" -> rbSpring.isChecked = true
                "SUMMER" -> rbSummer.isChecked = true
                "FALL" -> rbFall.isChecked = true
                else -> { }
            }
        }

}