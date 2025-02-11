package com.example.community1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class HomeFragment : Fragment() {

    private lateinit var btnSelectImages: Button
    private lateinit var imageContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_home, container, false)  // Ensure you have `fragment_home.xml`
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSelectImages = view.findViewById(R.id.btnSelectImages)
        imageContainer = view.findViewById(R.id.imageContainer)

        val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                val clipData = result.data?.clipData
                if (clipData != null) {
                    for (i in 0 until clipData.itemCount) {
                        val imageUri: Uri = clipData.getItemAt(i).uri
                        saveImageToStorage(imageUri)
                    }
                } else {
                    val imageUri: Uri? = result.data?.data
                    if (imageUri != null) {
                        saveImageToStorage(imageUri)
                    }
                }
            }
        }

        btnSelectImages.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            imagePickerLauncher.launch(intent)
        }

        // Load saved images on app restart
        loadSavedImages()
    }

    private fun saveImageToStorage(imageUri: Uri) {
        try {
            // Get an InputStream for the image
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(imageUri)

            // Create a new file in internal storage to save the image
            val imageFile = File(requireContext().filesDir, "image_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(imageFile)

            inputStream?.copyTo(outputStream)

            // Add the image to the container (display the saved image)
            addImageToContainer(Uri.fromFile(imageFile))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addImageToContainer(imageUri: Uri) {
        val imageView = ImageView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,  // Full width
                ViewGroup.LayoutParams.WRAP_CONTENT   // Height auto-adjusts
            ).apply {
                setMargins(0, 0, 0, 0)  // Remove margins
            }
            scaleType = ImageView.ScaleType.FIT_XY  // Stretch to fill width
            setImageURI(imageUri)
        }
        imageContainer.addView(imageView)
    }

    private fun loadSavedImages() {
        // Get all files in the internal storage directory
        val imageDir = requireContext().filesDir
        val imageFiles = imageDir.listFiles()

        // Load and display all images in the directory
        imageFiles?.forEach { imageFile ->
            if (imageFile.exists()) {
                addImageToContainer(Uri.fromFile(imageFile))
            }
        }
    }
}
