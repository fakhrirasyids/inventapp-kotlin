package com.fakhrirasyids.inventapp.ui.editstocks

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fakhrirasyids.inventapp.data.models.Stocks
import com.fakhrirasyids.inventapp.databinding.ActivityEditStocksBinding
import com.fakhrirasyids.inventapp.ui.detail.DetailActivity.Companion.EXTRA_DETAIL
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class EditStocksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditStocksBinding

    private val stockExtra by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra(EXTRA_DETAIL)
        } else {
            intent?.getParcelableExtra(EXTRA_DETAIL, Stocks::class.java)
        }
    }

    private val editStocksViewModel: EditStocksViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditStocksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        setListeners()
    }

    private fun observeViewModel() {
        editStocksViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                showToast(errorMessage)
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }

            binding.edDate.setOnClickListener { v ->
                val c = Calendar.getInstance()
                val year = c[Calendar.YEAR]
                val month = c[Calendar.MONTH]
                val day = c[Calendar.DAY_OF_MONTH]

                val datePickerDialog = DatePickerDialog(
                    this@EditStocksActivity,
                    { _: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int ->
                        binding.edDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year1)

                        val calendar = Calendar.getInstance()
                        calendar.set(year1, monthOfYear, dayOfMonth)

                        val timestamp = calendar.timeInMillis
                        editStocksViewModel.timestampPicked.postValue(timestamp)
                    },
                    year, month, day
                )
                datePickerDialog.show()
            }

            btnSave.setOnClickListener {
                if (isValid()) {
                    editStocksViewModel.updateStockQuantity(
                        stockId = stockExtra?.id ?: 0,
                        newQuantity = edStocksAmount.text.toString().toInt(),
                        timestamp = editStocksViewModel.timestampPicked.value ?: 0L,
                        isAdded = binding.btnIncoming.isChecked
                    )

                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun isValid() =
        if (binding.edDate.text.toString().isEmpty() || binding.edStocksAmount.text.toString()
                .isEmpty()
        ) {
            showToast("Fill the form correctly!")
            false
        } else if (editStocksViewModel.timestampPicked.value == null) {
            showToast("Fill the form correctly!")
            false
        } else {
            true
        }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}