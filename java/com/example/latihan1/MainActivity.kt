package com.example.latihan1

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : Activity() {

    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private lateinit var myListData: MutableList<ListViewData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myListData = mutableListOf()
        searchView = findViewById(R.id.searchView)
        listView = findViewById(R.id.listView)

        val dataToExport = mutableListOf<String>()
        val myListAdapter = ListViewAdapter(this, myListData)

        val btnAdd = findViewById<Button>(R.id.addButton)
        val btnExport = findViewById<Button>(R.id.exportButton)
        val loadingView = findViewById<CustomLoadingView>(R.id.loadingView)
        loadingView.startAnimation()
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddDataActivity::class.java)
            startActivity(intent)
        }

        btnExport.setOnClickListener {
            for (item in myListAdapter.DataList) {
                val headerWithNewLine = "${item.header} - ${item.content}\n" // Menambahkan karakter baris baru
                dataToExport.add(headerWithNewLine)
            }

            HttpUtil.exportToCSV(this@MainActivity, dataToExport, "Dat.txt")
            HttpUtil.showMessageDialog(this, "Export Berhasil", "Silahkan cek file di folder Document.")
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = myListData.filter {
                    it.header.contains(newText.orEmpty(), ignoreCase = true)
                }

                val updatedAdapter = ListViewAdapter(this@MainActivity, filteredList)
                listView.adapter = updatedAdapter

                return true
            }
        })

        listView.setOnItemClickListener { parent, view, position, i ->
            val selectedUserId = myListData[position].id
            val intent = Intent(this@MainActivity, EditDataActivity::class.java)
            intent.putExtra("userId", selectedUserId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val urlStr = "http://${Config.ip}/api/data/tampildata.php"
                val requestMethod = "GET"

                val response = HttpUtil.httpRequest(urlStr, requestMethod, null)

                if (response != null) {
                    // Parsing data JSON dari response
                    val jsonResponse = JSONObject(response)
                    val data = jsonResponse.getJSONArray("result")

                    myListData.clear()

                    // Loop melalui data dan tambahkan nama pengguna ke userList
                    for (i in 0 until data.length()) {
                        val userObject = data.getJSONObject(i)
                        val id = userObject.getString("id")
                        val name = userObject.getString("jumlah")
                        val ket = userObject.getString("keterangan")
                        val fullName = "$id\n$name"
                        val url = "http://${Config.ip}/api/data/img/1-image.jpg"
                        val bitmap = HttpUtil.downloadImage(url)

                        if (bitmap != null) {
                            myListData.add(ListViewData(id, bitmap, ket, name))
                        } else {
                            // Tambahkan default image jika gagal mengunduh
                            val defaultImageBitmap = BitmapFactory.decodeResource(resources, R.drawable.baseline_add_task_24)
                            myListData.add(ListViewData(id, defaultImageBitmap, ket, name))
                        }

                        listView.adapter = ListViewAdapter(this@MainActivity, myListData)
                    }
                } else {
                    Log.e("OnSuccesCatch", "Error while processing data")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("OnError MainActivity 113", "Error while processing data: ${e.message}")
            }
        }
        Log.d("onResume", "Called")
    }
}
