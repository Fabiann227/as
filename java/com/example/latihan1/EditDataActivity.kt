package com.example.latihan1

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class EditDataActivity : AppCompatActivity() {

    private lateinit var userId: String // ID pengguna yang dikirim dari MainActivity

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_data)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Atur ActionBar Anda sebagai ActionBar yang baru
        setSupportActionBar(toolbar)

        // Tambahkan tombol kembali (up) di ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        // Mendapatkan ID pengguna dari Intent
        userId = intent.getStringExtra("userId").toString()
        Log.d("Edit Data: userId", "UserId: $userId")

        // Inisialisasi elemen UI
        firstNameEditText = findViewById(R.id.editTextFirstName)
        lastNameEditText = findViewById(R.id.editTextLastName)
        saveButton = findViewById(R.id.buttonSave)
        deleteButton = findViewById(R.id.buttonDelete)

        val image = findViewById<ImageView>(R.id.photo)
        val apiUrl = "https://reqres.in/api/users/$userId"
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = HttpUtil.httpRequest(apiUrl, "GET", null)
                if (response != null) {
                    val userObject = JSONObject(response)
                    val firstName = userObject.getJSONObject("data").getString("first_name")
                    val lastName = userObject.getJSONObject("data").getString("last_name")
                    val pp = userObject.getJSONObject("data").getString("avatar")
                    val photo = HttpUtil.downloadImage(pp)

                    image.setImageBitmap(photo)
                    firstNameEditText.setText(firstName)
                    lastNameEditText.setText(lastName)
                } else {
                    Log.e("Get Single Data", "Error while processing data")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("OnError", "Error while processing data: ${e.message} 2")
            }
        }

        saveButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val apiPut = "http://${Config.ip}/api/data/updatedata.php/?id=15"
                    val updatedFirstName = firstNameEditText.text.toString()
                    val updatedLastName = lastNameEditText.text.toString()

                    val requestBody = JSONObject()
                    requestBody.put("keterangan", updatedFirstName)
                    requestBody.put("tipe", updatedLastName)

                    val jsonBody = requestBody.toString()

                    val result = HttpUtil.httpRequest(apiPut, "PUT", jsonBody)
                    if (result != null) {
                        onBackPressed()
                        Toast.makeText(applicationContext, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("OnError", "Error while processing data: ${e.message}")
                }
            }
        }

        deleteButton.setOnClickListener {
            val apiDel = "http://${Config.ip}/api/data/hapusdata.php/?id=55" // Ganti dengan URL yang sesuai
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val result = HttpUtil.httpRequest(apiDel, "DELETE", null)
                    if (result != null) {
                        Toast.makeText(applicationContext, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Gagal Menghapus data", Toast.LENGTH_SHORT).show()
                        Log.e("OnError", "Data tidak ada atau ada kesalahan")
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
