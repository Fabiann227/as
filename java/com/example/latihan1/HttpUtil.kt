package com.example.latihan1

import android.app.AlertDialog
import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import java.io.OutputStream
import java.lang.StringBuilder

class HttpUtil {
    companion object {
        suspend fun httpRequest(urlStr: String, requestMethod: String, requestBody: String?): String? {
            return withContext(Dispatchers.IO) {
                try {
                    val url = URL(urlStr)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = requestMethod
                    connection.connectTimeout = 5000

                    if (requestMethod == "POST" || requestMethod == "PUT")
                    {
                        connection.setRequestProperty("Content-Type", "application/json")
                        connection.doOutput = true

                        requestBody?.let {
                            val outputStream = OutputStreamWriter(connection.outputStream)
                            outputStream.write(requestBody)
                            outputStream.flush()
                            outputStream.close()
                        }
                    }

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK)
                    {
                        val reader = BufferedReader(InputStreamReader(connection.inputStream))
                        val response = StringBuilder()
                        var line: String?
                        while (reader.readLine().also { line = it } != null)
                        {
                            response.append(line)
                        }

                        reader.close()
                        response.toString()
                    }
                    else
                    {
                        null
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
        suspend fun downloadImage(urlStr: String): Bitmap? {
            return withContext(Dispatchers.IO) {
                try {
                    val connection = URL(urlStr).openConnection() as HttpURLConnection
                    connection.connect()

                    val inputStream = connection.inputStream
                    return@withContext BitmapFactory.decodeStream(inputStream)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    return@withContext null
                }
            }
        }

        fun showMessageDialog(ctx: Context, title: String, message: String) {
            val builder = AlertDialog.Builder(ctx)
            builder.setTitle(title)
            builder.setMessage(message)

            builder.setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        fun exportToCSV(context: Context, dataList: List<String>, fileName: String) {
            val resolver = context.contentResolver

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
            }

            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

            try {
                uri?.let {
                    val outputStream: OutputStream? = resolver.openOutputStream(it)
                    outputStream?.use {
                        for (data in dataList) {
                            it.write(data.toByteArray())
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}