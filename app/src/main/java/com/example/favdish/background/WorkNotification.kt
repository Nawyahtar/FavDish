package com.example.favdish.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.favdish.R
import com.example.favdish.model.database.FavDishRoomDatabase
import com.example.favdish.view.activities.MainActivity


class WorkNotification(context: Context, workerParameter: WorkerParameters) :
    Worker(context, workerParameter) {

    companion object {
        private const val CHANNEL_ID = "channel Id"
        private const val NOTIFICATION_ID = 1
    }

    override fun doWork(): Result {
        Log.i("Notification","Success")
        val data = FavDishRoomDatabase.getDatabase(applicationContext).favDishDao().selectALL()
        if (data.isNotEmpty()){
            doNotification()
        }else{
            alertNotification()
        }
        return Result.success()
    }

    private fun doNotification() {
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.navigation_random_dish)
            .createPendingIntent()
        val text = "Recommend Dish For You is ----"
        notification(text, pendingIntent)

    }

    private fun alertNotification(){
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val text = "Welcome from FavDish.You need to add dish first"
        notification(text, pendingIntent)
    }


    private fun notification(text: String, pendingIntent : PendingIntent){
        val notification = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("All Dishes")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName = "Channel Name"
            val channelDescription = "Channel Description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            )as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(applicationContext)){
            notify(NOTIFICATION_ID,notification.build())
        }
    }

}