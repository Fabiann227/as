package com.example.latihan1

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class Function {
    companion object {
        suspend fun HttpReq(urlStr: String, requestMethod: String, jsonBody: String): String? {
            return withContext(Dispatchers.IO) {
                try {
                    val url = URL(urlStr)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = requestMethod
                    connection.connectTimeout = 5000

                    if(requestMethod == "POST" || requestMethod == "PUT")
                    {
                        connection.setRequestProperty("Content-Type", "application/json")
                        connection.doOutput = true

                        jsonBody?.let {
                            val outputStream = OutputStreamWriter(connection.outputStream)
                            outputStream.write(jsonBody)
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
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                    null
                }
            }
        }
    }
}