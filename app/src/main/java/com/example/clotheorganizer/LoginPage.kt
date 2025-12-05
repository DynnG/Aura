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

class LoginPage : AppCompatActivity() {
    private lateinit var forgotpassword: Button
    private lateinit var signUpButton: Button
    private lateinit var loginButton: Button
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout

    private lateinit var dbHelper: AuraDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AuraDBHelper(this)

        forgotpassword = findViewById(R.id.forgotpassword)
        signUpButton = findViewById(R.id.signUpButton)
        loginButton = findViewById(R.id.loginButton)
        usernameLayout = findViewById(R.id.username)
        passwordLayout = findViewById(R.id.password)

        forgotpassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpPage::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val username = usernameLayout.editText?.text.toString().trim()
            val password = passwordLayout.editText?.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = dbHelper.checkUser(username, password)

            if (user != null) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                
                // Save User ID to SharedPreferences
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                user.userID?.let { editor.putInt("USER_ID", it) }
                editor.apply()

                val intent = Intent(this, dashboard::class.java)
                startActivity(intent)
                finish() // Finish LoginPage so the user can't go back to it
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
