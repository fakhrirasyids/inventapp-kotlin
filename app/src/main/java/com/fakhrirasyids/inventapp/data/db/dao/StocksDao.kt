package com.fakhrirasyids.inventapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fakhrirasyids.inventapp.data.models.Stocks

@Dao
interface StocksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: Stocks): Long

    @Update
    suspend fun updateStock(stock: Stocks)

    @Delete
    suspend fun deleteStock(stock: Stocks)

    @Query("SELECT * FROM stocks WHERE id = :stockId")
    suspend fun getStockById(stockId: Int): Stocks?

    @Query("SELECT * FROM stocks")
    fun getAllStocks(): LiveData<List<Stocks>>

    @Query("UPDATE stocks SET quantity = :newQuantity WHERE id = :stockId")
    suspend fun updateStockQuantity(stockId: Int, newQuantity: Int)
}
