package com.example.cantlai

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import android.os.Build

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startBtn = findViewById<Button>(R.id.startBubbleBtn)

        startBtn.setOnClickListener {
            // Check if we have the "Draw over other apps" permission
            if (!Settings.canDrawOverlays(this)) {
                // Permission is not granted, send user to settings
                Toast.makeText(this, getString(R.string.allow_overlay_permission), Toast.LENGTH_LONG).show()
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName"),
                )
                startActivity(intent)
            } else {
                // Permission already granted, start the service!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(
                        Intent(this, FloatingBubbleService::class.java)
                    )
                } else {
                    startService(
                        Intent(this, FloatingBubbleService::class.java)
                    )
                }

                // Close the app so the user can see the bubble on their home screen
                finish()
            }
        }
    }
}
