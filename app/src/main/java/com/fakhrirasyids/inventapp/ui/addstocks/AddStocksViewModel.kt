package com.fakhrirasyids.inventapp.ui.addstocks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhrirasyids.inventapp.data.models.Stocks
import com.fakhrirasyids.inventapp.data.repo.InventoryRepository
import kotlinx.coroutines.launch
import java.io.File

class AddStocksViewModel(private val repository: InventoryRepository) :
    ViewModel() {
    val imageFile = MutableLiveData<File>()

    fun addStock(stocks: Stocks) {
        viewModelScope.launch {
            repository.addStock(stocks)
        }
    }
}