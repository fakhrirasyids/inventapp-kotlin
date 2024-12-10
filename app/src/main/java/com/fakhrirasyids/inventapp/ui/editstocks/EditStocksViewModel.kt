package com.fakhrirasyids.inventapp.ui.editstocks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.fakhrirasyids.inventapp.data.repo.InventoryRepository
import kotlinx.coroutines.launch

class EditStocksViewModel(private val repository: InventoryRepository) :
    ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    val timestampPicked = MutableLiveData<Long>()

    fun updateStockQuantity(stockId: Int, newQuantity: Int, timestamp: Long, isAdded: Boolean) {
        viewModelScope.launch {
            try {
                _errorMessage.postValue("")
                repository.updateStockQuantity(stockId, newQuantity, timestamp, isAdded)
            } catch (e: IllegalArgumentException) {
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                _errorMessage.postValue("An unexpected error occurred: ${e.localizedMessage}")
            }
        }
    }
}