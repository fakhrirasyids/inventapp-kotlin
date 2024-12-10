package com.fakhrirasyids.inventapp.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Constants {
    // Converter
    val Long.asFormattedDate: String
        get() {
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
            return dateFormat.format(Date(this))
        }

    val Double.asRupiah: String
        get() {
            val rupiahFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            return rupiahFormat.format(this)
        }

}