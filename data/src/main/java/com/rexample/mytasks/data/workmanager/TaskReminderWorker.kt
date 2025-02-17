package com.rexample.mytasks.data.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rexample.mytasks.data.R

class TaskReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val taskName = inputData.getString("TASK_NAME") ?: "Tugas"
        val taskId = inputData.getInt("TASK_ID", 0)
        val userId = inputData.getInt("USER_ID", 0)

        showNotification(taskName, taskId, userId)
        return Result.success()
    }

    private fun showNotification(taskName: String, taskId: Int, userId: Int) {
        val channelId = "task_reminder_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Pengingat Tugas",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.mytasks_icon)
            .setContentTitle("Pengingat Tugas")
            .setContentText("Tugas \"$taskName\" akan berakhir!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))
            .setAutoCancel(true)

        notificationManager.notify(taskId + userId * 1000, builder.build())
    }
}
