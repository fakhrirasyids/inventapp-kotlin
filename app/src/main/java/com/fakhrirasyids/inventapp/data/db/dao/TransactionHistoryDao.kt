package com.fakhrirasyids.inventapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fakhrirasyids.inventapp.data.models.TransactionHistory

@Dao
interface TransactionHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionHistory): Long

    @Query("SELECT * FROM transaction_history WHERE stockId = :stockId")
    fun getTransactionHistoryForStock(stockId: Int): LiveData<List<TransactionHistory>>

    @Query("SELECT * FROM transaction_history ORDER BY timestamp DESC")
    fun getAllTransactionHistory(): LiveData<List<TransactionHistory>>

    @Query("SELECT * FROM transaction_history WHERE isAdded = :isAdded ORDER BY timestamp DESC")
    fun getTransactionHistoryByIsAdded(isAdded: Boolean): LiveData<List<TransactionHistory>>
}