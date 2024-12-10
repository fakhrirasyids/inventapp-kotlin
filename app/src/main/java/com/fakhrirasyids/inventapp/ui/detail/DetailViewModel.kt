package com.fakhrirasyids.inventapp.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhrirasyids.inventapp.data.models.Stocks
import com.fakhrirasyids.inventapp.data.models.TransactionHistory
import com.fakhrirasyids.inventapp.data.repo.InventoryRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: InventoryRepository) :
    ViewModel() {
    val stockFetched = MutableLiveData<Stocks>()

    fun getTransactionHistoryForStock(stockId: Int) =
        repository.getTransactionHistoryForStock(stockId)

    fun getStock(stockId: Int) {
        viewModelScope.launch {
            try {
                val stocksList = repository.getStockById(stockId)
                stockFetched.postValue(stocksList ?: Stocks())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}