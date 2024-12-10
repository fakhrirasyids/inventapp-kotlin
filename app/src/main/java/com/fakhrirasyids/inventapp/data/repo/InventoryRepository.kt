package com.fakhrirasyids.inventapp.data.repo

import androidx.lifecycle.LiveData
import com.fakhrirasyids.inventapp.data.db.InventappDatabase
import com.fakhrirasyids.inventapp.data.models.Stocks
import com.fakhrirasyids.inventapp.data.models.TransactionHistory
import kotlinx.coroutines.runBlocking
import kotlin.math.abs

class InventoryRepository(inventappDatabase: InventappDatabase) {

    private val stocksDao = inventappDatabase.stocksDao()
    private val transactionHistoryDao = inventappDatabase.transactionHistoryDao()

    suspend fun addStock(stock: Stocks): Long {
        val stockId = stocksDao.insertStock(stock)
        if (stock.quantity > 0) {
            recordTransaction(
                stockId.toInt(),
                stock.quantity,
                isAdded = true,
                title = stock.title,
                image = stock.image,
                timestamp = System.currentTimeMillis()
            )
        }
        return stockId
    }

    suspend fun updateStock(stock: Stocks) {
        stocksDao.updateStock(stock)
    }

    suspend fun deleteStock(stock: Stocks) {
        stocksDao.deleteStock(stock)
    }

    suspend fun getStockById(stockId: Int): Stocks? {
        return stocksDao.getStockById(stockId)
    }

    fun getAllStocks(): LiveData<List<Stocks>> {
        return stocksDao.getAllStocks()
    }

    suspend fun updateStockQuantity(
        stockId: Int,
        newQuantity: Int,
        timestamp: Long,
        isAdded: Boolean
    ) {
        val currentStock = runBlocking { stocksDao.getStockById(stockId) }
            ?: throw IllegalArgumentException("Stock with ID $stockId not found")

        val tempQty = if (isAdded) {
            currentStock.quantity + newQuantity
        } else {
            currentStock.quantity - newQuantity
        }

        if (tempQty < 0) {
            throw IllegalArgumentException("New quantity cannot be negative")
        }

        val oldQuantity = currentStock.quantity
        if (oldQuantity != newQuantity) {
            stocksDao.updateStockQuantity(stockId, tempQty)
            recordTransaction(
                stockId,
                newQuantity,
                isAdded = isAdded,
                title = currentStock.title,
                image = currentStock.image,
                timestamp = timestamp
            )
        }
    }

    private suspend fun recordTransaction(
        stockId: Int,
        quantity: Int,
        isAdded: Boolean,
        title: String,
        image: String,
        timestamp: Long
    ) {
        val transaction = TransactionHistory(
            stockId = stockId,
            isAdded = isAdded,
            quantity = quantity,
            timestamp = timestamp,
            title = title,
            image = image
        )
        transactionHistoryDao.insertTransaction(transaction)
    }

    fun getTransactionHistoryForStock(stockId: Int) =
        transactionHistoryDao.getTransactionHistoryForStock(stockId)

    fun getAllTransactionHistory() =
        transactionHistoryDao.getAllTransactionHistory()

    fun getTransactionHistoryByIsAdded(isAdded: Boolean) =
        transactionHistoryDao.getTransactionHistoryByIsAdded(isAdded)
}

