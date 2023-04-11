package org.lucpc.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession

object ConnectionUtil {
    data class HttpResponse(val content: String, val response: Int)
    private const val USER_AGENT = "Leetle-Bot"

    fun httpsConnection(url: String, vararg headers: Pair<String, String>): HttpResponse? {
        try {
            var url = url
            if (headers.isNotEmpty()) {
                url += "?"
                var first = true
                for ((key, value) in headers) {
                    if (!first)
                        url += "&"
                    url += "$key=$value"
                    first = false
                }
            }
            val connection = URL(url).openConnection() as HttpsURLConnection
            connection.hostnameVerifier = AllHostnameVerifier()
            connection.setRequestProperty("User-Agent", USER_AGENT)
            connection.connect()
            return HttpResponse(inputStreamToString(connection.inputStream), connection.responseCode)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun inputStreamToString(inputStream: InputStream): String {
        val stringBuilder = StringBuilder()
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line).append(System.lineSeparator())
        }
        bufferedReader.close()
        return stringBuilder.toString()
    }

    class AllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }
}