package com.example.cantlai

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import kotlin.math.abs

class FloatingBubbleService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var params: WindowManager.LayoutParams

    companion object {
        private const val CHANNEL_ID = "FloatingBubbleChannel"
        private const val NOTIFICATION_ID = 1
    }

    @SuppressLint("InflateParams")
    override fun onCreate() {
        super.onCreate()

        // 1. Create the notification channel (Required for Android 8.0+)
        createNotificationChannel()

        // 2. Start as a Foreground Service so Android doesn't kill it
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
        }
        val notification = builder
            .setContentTitle("CantL-AI")
            .setContentText("Floating bubble is active")
            .setSmallIcon(android.R.drawable.ic_menu_search)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE,
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        // 3. Draw the bubble
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_bubble, null)

        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 300

        windowManager.addView(floatingView, params)
        setupDragAndClick()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Floating Bubble Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupDragAndClick() {
        val bubbleIcon = floatingView.findViewById<ImageView>(R.id.bubble_icon)

        bubbleIcon.isClickable = true

        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f
        var isDragging = false

        bubbleIcon.setOnTouchListener { view, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isDragging = false
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX - initialTouchX
                    val dy = event.rawY - initialTouchY

                    if (abs(dx) > 10 || abs(dy) > 10) {
                        isDragging = true
                    }

                    if (isDragging) {
                        params.x = initialX + dx.toInt()
                        params.y = initialY + dy.toInt()

                        windowManager.updateViewLayout(floatingView, params)
                    }

                    true
                }

                MotionEvent.ACTION_UP -> {
                    Toast.makeText(
                        this,
                        "TOUCH RELEASE DETECTED",
                        Toast.LENGTH_LONG
                    ).show()

                    if (!isDragging) {
                        onBubbleClicked()
                    }

                    true
                }

                MotionEvent.ACTION_CANCEL -> true

                else -> false
            }
        }

    }


    private fun onBubbleClicked() {
        Toast.makeText(
            this,
            "BUBBLE CLICKED SUCCESSFULLY!",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView.isInitialized) {
            windowManager.removeView(floatingView)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
