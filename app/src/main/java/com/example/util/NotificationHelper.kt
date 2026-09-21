package com.example.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.model.DailyPostItem

object NotificationHelper {
  const val CHANNEL_ID = "daily_admin_posts_channel"
  const val CHANNEL_NAME = "Daily Wealth & Insurance Bulletins"
  const val CHANNEL_DESC = "Instant pop-up bulletins and market advisories posted by IRDAI Admin"

  fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_HIGH
      val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
        description = CHANNEL_DESC
        enableVibration(true)
        vibrationPattern = longArrayOf(0, 250, 100, 250)
        setShowBadge(true)
      }
      val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

  fun sendDailyPostNotification(context: Context, post: DailyPostItem): Boolean {
    createNotificationChannel(context)

    // Check permission on Android 13+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      val permissionCheck = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
      )
      if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
        return false
      }
    }

    val intent = Intent(context, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
      putExtra("EXTRA_POST_ID", post.id)
    }

    val pendingIntent = PendingIntent.getActivity(
      context,
      post.id.hashCode(),
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
    )

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentTitle("📢 ${post.title}")
      .setContentText(post.content)
      .setStyle(
        NotificationCompat.BigTextStyle()
          .bigText(post.content)
          .setSummaryText(post.categoryTag)
      )
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setDefaults(NotificationCompat.DEFAULT_ALL)
      .setAutoCancel(true)
      .setContentIntent(pendingIntent)
      .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
      .build()

    try {
      NotificationManagerCompat.from(context).notify(post.id.hashCode(), notification)
      return true
    } catch (e: SecurityException) {
      return false
    } catch (e: Exception) {
      return false
    }
  }
}
