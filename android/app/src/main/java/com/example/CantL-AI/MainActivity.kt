package com.example.kantlai

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

val androidx: Any

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            // For demo, we send a static claim. In a real app, you'd capture user input.
            val claim = "COVID-19 vaccines cause infertility"
            checkClaim(claim)
        }
    }

    private fun checkClaim(claim: String) {
        val json = JSONObject().apply { put("claim", claim) }
        val requestBody = RequestBody.create(MediaType.get("application/json"), json.toString())
        val request = Request.Builder()
            .url("http://10.0.2.2:8000/fact_check") // 10.0.2.2 points to host machine from emulator
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show() }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: "{}"
                val jsonResp = JSONObject(body)
                val truth = jsonResp.optDouble("truthfulness", -1.0)
                val message = if (truth >= 0) {
                    val verdict = when {
                        truth > 0.66 -> "Likely True"
                        truth > 0.33 -> "Mixed"
                        else -> "Likely False"
                    }
                    "Result: $verdict (Confidence ${"%.1f".format(truth * 100)}%)"
                } else "Invalid response"
                runOnUiThread { Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show() }
            }
        })
    }
}
