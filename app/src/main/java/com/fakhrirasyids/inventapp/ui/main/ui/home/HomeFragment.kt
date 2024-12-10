package com.fakhrirasyids.inventapp.ui.main.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhrirasyids.inventapp.R
import com.fakhrirasyids.inventapp.databinding.FragmentHomeBinding
import com.fakhrirasyids.inventapp.ui.adapters.TransactionAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModel()

    private val transactionsAdapter = TransactionAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        observeViewModel()
        setViews()

        return root
    }

    private fun observeViewModel() {
        homeViewModel.tabFlag.observe(viewLifecycleOwner) { tabFlag ->
            if (tabFlag == 1) {
                lifecycleScope.launch {
                    homeViewModel.filterTransactionHistory(true).asFlow().collect {
                        binding.rvTransactions.isVisible = it.isNotEmpty()
                        binding.tvEmptyTransaction.isVisible = it.isEmpty()
                        binding.tvTransaction.text = getString(R.string.empty_incoming)
                        transactionsAdapter.submitList(it)
                    }
                }

                binding.btnIncomeTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryBlue
                    )
                )
                binding.btnIncomeTab.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.btnExpenseTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.btnExpenseTab.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryBlue
                    )
                )
            } else if (tabFlag == 2) {
                lifecycleScope.launch {
                    homeViewModel.filterTransactionHistory(false).asFlow().collect {
                        binding.rvTransactions.isVisible = it.isNotEmpty()
                        binding.tvEmptyTransaction.isVisible = it.isEmpty()
                        binding.tvTransaction.text = getString(R.string.empty_outgoing)
                        transactionsAdapter.submitList(it)
                    }
                }

                binding.btnIncomeTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.btnIncomeTab.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryBlue
                    )
                )
                binding.btnExpenseTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryBlue
                    )
                )
                binding.btnExpenseTab.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            } else {
                lifecycleScope.launch {
                    homeViewModel.fetchAllTransactionHistory().asFlow().collect {
                        binding.rvTransactions.isVisible = it.isNotEmpty()
                        binding.tvEmptyTransaction.isVisible = it.isEmpty()
                        binding.tvTransaction.text = getString(R.string.empty_transaction)
                        transactionsAdapter.submitList(it)
                    }
                }

                binding.btnIncomeTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.btnIncomeTab.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryBlue
                    )
                )
                binding.btnExpenseTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.btnExpenseTab.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryBlue
                    )
                )
            }
        }
    }

    private fun setViews() {
        binding.rvTransactions.adapter = transactionsAdapter
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())

        binding.btnIncomeTab.setOnClickListener {
            if (homeViewModel.tabFlag.value == 1) {
                homeViewModel.tabFlag.postValue(0)
            } else {
                homeViewModel.tabFlag.postValue(1)
            }
        }

        binding.btnExpenseTab.setOnClickListener {
            if (homeViewModel.tabFlag.value == 2) {
                homeViewModel.tabFlag.postValue(0)
            } else {
                homeViewModel.tabFlag.postValue(2)
            }
        }
    }
}