package com.example.campaccessoriesinformation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class EditIteamActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var typeSpinner: Spinner
    private lateinit var descriptionEditText: EditText
    private lateinit var pricePerDayEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var imageView: ImageView
    private lateinit var databaseHelper: DatabaseHelper

    private var iteamId: Int = -1
    private var imagePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_iteam)
        imageView = findViewById(R.id.iteam_image)
        nameEditText = findViewById(R.id.edit_text_name)
        typeSpinner = findViewById(R.id.spinner_type)
        descriptionEditText = findViewById(R.id.edit_text_description)
        pricePerDayEditText = findViewById(R.id.edit_text_price_per_day)
        updateButton = findViewById(R.id.button_update)
        databaseHelper = DatabaseHelper(this)

        // Get data passed from MainActivity
        val extras = intent.extras
        if (extras != null) {
            iteamId = extras.getInt("id")
            nameEditText.setText(extras.getString("name"))
            val type = extras.getString("type")
            val types = arrayOf("Merch", "CampingBackpack", "CampingEquipment", "CampingTent")
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            typeSpinner.adapter = adapter
            typeSpinner.setSelection(types.indexOf(type))

            descriptionEditText.setText(extras.getString("description"))
            pricePerDayEditText.setText(extras.getDouble("pricePerDay").toString())
            imagePath = extras.getString("image", "")
            Glide.with(this).load(imagePath).into(imageView)
        }
        imageView.setOnClickListener {
            // Open the gallery to select a new image
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
        updateButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val type = typeSpinner.selectedItem.toString()
            val description = descriptionEditText.text.toString()
            val pricePerDay = pricePerDayEditText.text.toString().toDouble()

            if (name.isNotEmpty() && description.isNotEmpty() && pricePerDay >= 0) {
                databaseHelper.updateIteam(iteamId, name, type, description, pricePerDay,imagePath)
                finish() // Close activity after updating vehicle
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected image URI
            val imageUri = data.data
            // Load and display the selected image using Glide
            Glide.with(this).load(imageUri).into(imageView)
            // Save the image path
            imagePath = imageUri.toString()
        }
    }
    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }
}