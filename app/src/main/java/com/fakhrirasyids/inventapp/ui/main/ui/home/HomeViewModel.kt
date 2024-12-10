package com.fakhrirasyids.inventapp.ui.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhrirasyids.inventapp.data.models.TransactionHistory
import com.fakhrirasyids.inventapp.data.repo.InventoryRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val inventoryRepository: InventoryRepository) : ViewModel() {
    val tabFlag = MutableLiveData(0)

    init {
        fetchAllTransactionHistory()
    }

    fun fetchAllTransactionHistory() = inventoryRepository.getAllTransactionHistory()

    fun filterTransactionHistory(isAdded: Boolean) =
        inventoryRepository.getTransactionHistoryByIsAdded(isAdded)
}