package com.example.clotheorganizer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout

class SignUpPage : AppCompatActivity() {
    private lateinit var su_backBttn: Button
    private lateinit var su_get_started_bttn: Button
    private lateinit var su_fullname_layout: TextInputLayout
    private lateinit var su_username_layout: TextInputLayout
    private lateinit var su_password_layout: TextInputLayout
    private lateinit var su_confirm_password_layout: TextInputLayout

    private lateinit var dbHelper: AuraDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AuraDBHelper(this)

        su_backBttn = findViewById(R.id.su_backBttn)
        su_get_started_bttn = findViewById(R.id.su_get_started_bttn)
        su_fullname_layout = findViewById(R.id.su_fullname)
        su_username_layout = findViewById(R.id.su_username)
        su_password_layout = findViewById(R.id.su_password_layout)
        su_confirm_password_layout = findViewById(R.id.su_confirm_password_layout)

        su_backBttn.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        su_get_started_bttn.setOnClickListener {
            val fullName = su_fullname_layout.editText?.text.toString().trim()
            val username = su_username_layout.editText?.text.toString().trim()
            val password = su_password_layout.editText?.text.toString().trim()
            val confirmPassword = su_confirm_password_layout.editText?.text.toString().trim()

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // You can add more specific validation here (e.g., password strength)

            val user = User(userName = username, password = password, fullname = fullName, email = "") // Email is not in the form, so passing empty string
            val success = dbHelper.addUser(user)

            if (success) {
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
                finish() // Finish SignUpPage so the user can't go back to it
            } else {
                Toast.makeText(this, "Failed to create account. Username might already exist.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
