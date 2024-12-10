package com.fakhrirasyids.inventapp.ui.addstocks

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.fakhrirasyids.inventapp.R
import com.fakhrirasyids.inventapp.data.models.Stocks
import com.fakhrirasyids.inventapp.databinding.ActivityAddStocksBinding
import com.fakhrirasyids.inventapp.ui.customview.ImagePickerBottomSheetFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class AddStocksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStocksBinding

    private val addStocksViewModel: AddStocksViewModel by viewModel()

    private lateinit var imagePathLocation: String

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            (result.data?.data as Uri).let { uri ->
                addStocksViewModel.imageFile.postValue(reduceFileSize(uri.toFile()))
            }
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            File(imagePathLocation).let { image ->
                rotateImage(BitmapFactory.decodeFile(image.path), imagePathLocation).compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    FileOutputStream(image)
                )

                addStocksViewModel.imageFile.postValue(reduceFileSize(image))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStocksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeImage()

        setListeners()
    }

    private fun observeImage() {
        addStocksViewModel.imageFile.observe(this@AddStocksActivity) {
            binding.layoutStock.isVisible = it != null
            binding.ivStock.setImageBitmap(BitmapFactory.decodeFile(it.path))
        }
    }

    private fun setListeners() {
        binding.apply {
            btnBack.setOnClickListener { finish() }

            edReceipt.setOnClickListener {
                openImagePicker()
            }

            btnSave.setOnClickListener {
                if (isValid()) {
                    val stocks = Stocks(
                        title = edTitle.text.toString(),
                        category = edCategory.text.toString(),
                        description = edDesc.text.toString(),
                        price = edPrice.text.toString().toDouble(),
                        image = Uri.fromFile(addStocksViewModel.imageFile.value).toString(),
                    )

                    addStocksViewModel.addStock(stocks)
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun openImagePicker() {
        val bottomSheetFragment = ImagePickerBottomSheetFragment()
        bottomSheetFragment.setOnButtonClickListener(object :
            ImagePickerBottomSheetFragment.OnImagePickerClickListener {
            @SuppressLint("QueryPermissionsNeeded")
            override fun cameraClick() {
                if (checkImagePermission()) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.resolveActivity(packageManager)
                    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val customTempFile = File.createTempFile(
                        SimpleDateFormat(
                            "dd-MMM-yyyy",
                            Locale.US
                        ).format(System.currentTimeMillis()), ".jpg", storageDir
                    )
                    customTempFile.also {
                        imagePathLocation = it.absolutePath
                        intent.putExtra(
                            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                                this@AddStocksActivity,
                                this@AddStocksActivity.application.packageName,
                                it
                            )
                        )
                        cameraLauncher.launch(intent)
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        this@AddStocksActivity,
                        REQUIRED_CAMERA_PERMISSION,
                        REQUEST_CODE_PERMISSIONS
                    )
                }
            }

            override fun galleryClick() {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                }
                val chooser = Intent.createChooser(intent, "Pick an Image")
                galleryLauncher.launch(chooser)
            }
        })

        bottomSheetFragment.show(supportFragmentManager, "ReceiptBottomSheet")
    }


    private fun rotateImage(bitmap: Bitmap, path: String): Bitmap {
        val matrix = Matrix()
        when (ExifInterface(path).getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
        }

        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

    private fun Uri.toFile(): File {
        val tempFile = File.createTempFile(
            "IMG_${System.currentTimeMillis()}_",
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        contentResolver.openInputStream(this)?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }

    private fun reduceFileSize(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun isValid() = if (binding.edTitle.text.isNullOrEmpty()) {
        showToast(ContextCompat.getString(this, R.string.title_invalid))
        false
    } else if (binding.edDesc.text.isNullOrEmpty()) {
        showToast(ContextCompat.getString(this, R.string.desc_invalid))
        false
    } else if (binding.edCategory.text.isNullOrEmpty()) {
        showToast(ContextCompat.getString(this, R.string.category_invalid))
        false
    } else if (binding.edPrice.text.isNullOrEmpty()) {
        showToast(ContextCompat.getString(this, R.string.price_invalid))
        false
    } else if (addStocksViewModel.imageFile.value == null) {
        showToast(ContextCompat.getString(this, R.string.image_invalid))
        false
    } else {
        true
    }

    private fun checkImagePermission() = REQUIRED_CAMERA_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



    companion object {
        private val REQUIRED_CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 100
    }
}