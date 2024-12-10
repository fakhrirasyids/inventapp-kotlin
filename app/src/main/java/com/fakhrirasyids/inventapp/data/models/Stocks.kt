package com.fakhrirasyids.inventapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stocks")
data class Stocks(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var category: String = "",
    var title: String = "",
    var image: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var quantity: Int = 0
) : Parcelable
