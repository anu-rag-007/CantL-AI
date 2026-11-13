package com.example.cantlai

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cantlai.FloatingBubbleService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startBtn = findViewById<Button>(R.id.startBubbleBtn)

        startBtn.setOnClickListener {
            // Check if we have the "Draw over other apps" permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                // Permission is not granted, send user to settings
                Toast.makeText(this, "Please allow 'Display over other apps'", Toast.LENGTH_LONG).show()
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            } else {
                // Permission already granted, start the service!
                startService(Intent(this, FloatingBubbleService::class.java))

                // Close the app so the user can see the bubble on their home screen
                finish()
            }
        }
    }
}