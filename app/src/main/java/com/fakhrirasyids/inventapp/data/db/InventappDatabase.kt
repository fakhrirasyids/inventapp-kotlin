package com.fakhrirasyids.inventapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fakhrirasyids.inventapp.data.db.dao.StocksDao
import com.fakhrirasyids.inventapp.data.db.dao.TransactionHistoryDao
import com.fakhrirasyids.inventapp.data.models.Stocks
import com.fakhrirasyids.inventapp.data.models.TransactionHistory

@Database(entities = [Stocks::class, TransactionHistory::class], version = 1, exportSchema = false)
abstract class InventappDatabase : RoomDatabase() {

    abstract fun stocksDao(): StocksDao
    abstract fun transactionHistoryDao(): TransactionHistoryDao

    companion object {
        @Volatile
        private var instance: InventappDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): InventappDatabase = instance ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                InventappDatabase::class.java, "Inventapp.DB"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
    }
}