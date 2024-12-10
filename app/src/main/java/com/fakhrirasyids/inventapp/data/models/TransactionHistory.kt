package com.fakhrirasyids.inventapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "transaction_history")
data class TransactionHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val stockId: Int,
    val isAdded: Boolean,
    val title: String,
    val image: String,
    val quantity: Int,
    val timestamp: Long
) : Parcelable