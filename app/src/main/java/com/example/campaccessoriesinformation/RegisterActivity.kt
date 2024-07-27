package com.example.campaccessoriesinformation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var mobileEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameEditText = findViewById(R.id.edit_text_name)
        mobileEditText = findViewById(R.id.edit_text_mobile)
        passwordEditText = findViewById(R.id.edit_text_password)
        registerButton = findViewById(R.id.button_register)
        databaseHelper = DatabaseHelper(this)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val mobile = mobileEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (name.isNotEmpty() && mobile.isNotEmpty() && password.isNotEmpty()) {
                val role = "user"
                val result = databaseHelper.insertUser(name, mobile, password, role)
                if (result != -1L) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
