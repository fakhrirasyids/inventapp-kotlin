package com.fakhrirasyids.inventapp.ui.main.ui.analytics

import androidx.lifecycle.ViewModel
import com.fakhrirasyids.inventapp.data.repo.InventoryRepository

class AnalyticsViewModel(private val inventoryRepository: InventoryRepository) : ViewModel() {
    fun fetchAllStocks() = inventoryRepository.getAllStocks()
}