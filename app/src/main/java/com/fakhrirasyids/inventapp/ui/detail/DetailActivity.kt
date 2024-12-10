package com.fakhrirasyids.inventapp.ui.detail

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhrirasyids.inventapp.R
import com.fakhrirasyids.inventapp.data.models.Stocks
import com.fakhrirasyids.inventapp.databinding.ActivityDetailBinding
import com.fakhrirasyids.inventapp.ui.adapters.TransactionAdapter
import com.fakhrirasyids.inventapp.ui.editstocks.EditStocksActivity
import com.fakhrirasyids.inventapp.utils.Constants.asRupiah
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel: DetailViewModel by viewModel()

    private val stockExtra by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra(EXTRA_DETAIL)
        } else {
            intent?.getParcelableExtra(EXTRA_DETAIL, Stocks::class.java)
        }
    }

    private val transactionAdapter = TransactionAdapter()

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            detailViewModel.getStock(stockExtra?.id ?: 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViews()
    }

    private fun setViews() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnAdd.setOnClickListener {
                val iEditStock = Intent(this@DetailActivity, EditStocksActivity::class.java)
                iEditStock.putExtra(EXTRA_DETAIL, stockExtra)
                activityLauncher.launch(iEditStock)
            }

            rvTransaction.apply {
                adapter = transactionAdapter
                layoutManager = LinearLayoutManager(this@DetailActivity)
            }

            stockExtra?.let {
                detailViewModel.getStock(it.id)

                detailViewModel.stockFetched.observe(this@DetailActivity) { detail ->
                    ivStock.setImageURI(Uri.parse(detail.image))
                    tvTitle.text = detail.title
                    tvCategory.text = StringBuilder("Category: ${detail.category}")
                    tvDesc.text = detail.description

                    tvInfo.text =
                        StringBuilder("${detail.price.asRupiah} | Stocks : ${detail.quantity}")
                }

                lifecycleScope.launch {
                    detailViewModel.getTransactionHistoryForStock(stockExtra?.id ?: 0).asFlow()
                        .collect { transactionHistory ->
                            tvEmptyTransaction.isVisible = transactionHistory.isEmpty()
                            rvTransaction.isVisible = transactionHistory.isNotEmpty()
                            transactionAdapter.submitList(transactionHistory.sortedBy { a -> a.timestamp }.reversed())
                            rvTransaction.smoothScrollToPosition(0)
                        }
                }
            }
        }
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}