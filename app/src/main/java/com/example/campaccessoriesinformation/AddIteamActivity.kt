package com.example.campaccessoriesinformation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddIteamActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var typeSpinner: Spinner
    private lateinit var descriptionEditText: EditText
    private lateinit var pricePerDayEditText: EditText
    private lateinit var addButton: Button
    private lateinit var iteamImage: ImageView
    private lateinit var selectImageButton: Button
    private var imageUri: Uri? = null
    private var selectedImagePath: String = ""
    private lateinit var databaseHelper: DatabaseHelper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_iteam)

        nameEditText = findViewById(R.id.edit_text_name)
        typeSpinner = findViewById(R.id.spinner_type)
        descriptionEditText = findViewById(R.id.edit_text_description)
        pricePerDayEditText = findViewById(R.id.edit_text_price_per_day)
        addButton = findViewById(R.id.button_add)
        iteamImage = findViewById(R.id.iteam_image)
        selectImageButton = findViewById(R.id.select_image_button)
        databaseHelper = DatabaseHelper(this)

        val types = arrayOf("Merch", "CampingBackpack", "CampingEquipment", "CampingTent")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = adapter

        selectImageButton.setOnClickListener {
            // Launch intent to pick image from gallery
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val type = typeSpinner.selectedItem.toString()
            val description = descriptionEditText.text.toString()
            val pricePerDay = pricePerDayEditText.text.toString().toDouble()

            if (name.isNotEmpty() && description.isNotEmpty() && pricePerDay >= 0) {
                databaseHelper.addIteam(name, type, description, pricePerDay,selectedImagePath)
                Toast.makeText(this, "Added Successful", Toast.LENGTH_SHORT).show()
                finish() // Close activity after adding vehicle
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null ) {
            val imageUri = data.data
//            selectedImagePath = getPathFromUri(imageUri!!)
            selectedImagePath = imageUri.toString()
            iteamImage.setImageURI(imageUri)
        }
    }
    private fun getPathFromUri(uri: Uri): String {
        var path = ""
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                path = it.getString(columnIndex)
            }
        }
        return path
    }
    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }
}
