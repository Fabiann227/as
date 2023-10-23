package com.example.latihan1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Atur ActionBar Anda sebagai ActionBar yang baru
        setSupportActionBar(toolbar)

        // Tambahkan tombol kembali (up) di ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        val firstNameEditText = findViewById<TextView>(R.id.editTextFirstName)
        val lastNameEditText = findViewById<TextView>(R.id.editTextLastName)

        val saveButton = findViewById<Button>(R.id.buttonSave)

        saveButton.setOnClickListener {
            val updatedFirstName = firstNameEditText.text.toString()
            val updatedLastName = lastNameEditText.text.toString()

            val apiUrl = "http://${Config.ip}/api/data/tambahdata.php/"

            val jsonBody = JSONObject()
            jsonBody.put("no_bukti", updatedFirstName)
            jsonBody.put("keterangan", updatedLastName)
            jsonBody.put("tipe", "JK")
            jsonBody.put("jumlah", "5000")

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val result = HttpUtil.httpRequest(apiUrl, "POST", jsonBody.toString())
                    if (result != null) {
                        Toast.makeText(applicationContext, "Sukses", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        Toast.makeText(applicationContext, "Terjadi kesalahan saat POST request", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("OnError", "Error while processing data: ${e.message}")
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}