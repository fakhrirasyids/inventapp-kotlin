package com.fakhrirasyids.inventapp.ui.main.ui.analytics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.fakhrirasyids.inventapp.databinding.FragmentAnalyticsBinding
import com.fakhrirasyids.inventapp.ui.adapters.StocksAdapter
import com.fakhrirasyids.inventapp.ui.addstocks.AddStocksActivity
import com.fakhrirasyids.inventapp.ui.detail.DetailActivity
import com.fakhrirasyids.inventapp.ui.detail.DetailActivity.Companion.EXTRA_DETAIL
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private val analyticsViewModel: AnalyticsViewModel by viewModel()

    private val stocksAdapter = StocksAdapter()

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
//            analyticsViewModel.fetchAllStocks()
            observeViewModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)

        observeViewModel()
        setViews()

        return binding.root
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            analyticsViewModel.fetchAllStocks().asFlow().collect {
                stocksAdapter.submitList(it)
                binding.rvStocks.smoothScrollToPosition(0)
                binding.tvEmtpyStocks.isVisible = it.isEmpty()
            }
        }
    }

    private fun setViews() {
        binding.apply {
            btnAddStock.setOnClickListener {
                activityLauncher.launch(Intent(requireContext(), AddStocksActivity::class.java))
            }

            stocksAdapter.onStocksClick = { stocks, _ ->
                val iDetail = Intent(requireContext(), DetailActivity::class.java)
                iDetail.putExtra(EXTRA_DETAIL, stocks)
                activityLauncher.launch(iDetail)
            }

            rvStocks.apply {
                adapter = stocksAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}