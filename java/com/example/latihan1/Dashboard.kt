package com.example.latihan1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import org.w3c.dom.Text

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tes)

        val txtname = findViewById<TextView>(R.id.txtName)
        val name = intent.getStringExtra("name")
        if (name != null) {
            txtname.setText(name)
        }

        val manageData = findViewById<CardView>(R.id.btnData)
        manageData.setOnClickListener {
            val intent = Intent(this@Dashboard, MainActivity::class.java)
            startActivity(intent)
        }
    }
}