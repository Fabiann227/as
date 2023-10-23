package com.example.latihan1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.txtUsername)
        val passwordEditText = findViewById<EditText>(R.id.txtPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val apiPut = "https://reqres.in/api/login"

                    val loginData = JSONObject()
                    loginData.put("email", username)
                    loginData.put("password", password)

                    val jsonBody = loginData.toString()

                    val result = HttpUtil.httpRequest(apiPut, "POST", jsonBody)
                    if (result != null) {
                        onBackPressed()
                        Toast.makeText(applicationContext, "Login Berhasil", Toast.LENGTH_SHORT).show()
                        finish()

                        val intent = Intent(this@LoginActivity, Dashboard::class.java)
                        intent.putExtra("name", username)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("OnError", "Error while processing data: ${e.message}")
                }
            }
        }
    }
}