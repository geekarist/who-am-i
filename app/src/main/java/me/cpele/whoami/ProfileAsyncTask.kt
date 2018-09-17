package me.cpele.whoami

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import java.io.InputStreamReader
import java.net.URL

class ProfileAsyncTask(private val application: Application?) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg accessTokenArray: String): String {
        val accessToken = accessTokenArray[0]
        val url = URL(
                "https://people.googleapis.com/v1/people/me" +
                        "?personFields=names" +
                        "&key=AIzaSyBOmTwHDWBiRnIF9-ByRJy6ed3ZXUA9wLQ"
        )
        val connection = url.openConnection().apply { setRequestProperty("Authorization", "Bearer $accessToken") }
        val reader = InputStreamReader(connection.getInputStream())
        return reader.readText()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        Toast.makeText(application, "Here is your response: $result", Toast.LENGTH_LONG).show()
    }
}
