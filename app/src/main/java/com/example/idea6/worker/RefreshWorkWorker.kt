package com.example.idea6.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Log.d
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.example.idea6.MainActivity
import com.example.idea6.R
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.random.nextInt

class RefreshWorkWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters

) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {

        //Refresh Word
        val save_index = File(context?.filesDir, "lastword.txt")
        var myRandomInt:Int = 0
        if(save_index.exists()){
            delete_index()
            myRandomInt = Random.nextInt(1..13161)
            write_to_file(myRandomInt)
        }else{
            myRandomInt = Random.nextInt(1..13161)
            write_to_file(myRandomInt)
        }
        d("Changed File", myRandomInt.toString())

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        var builder = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("New Word!!")
            .setContentText("Click to see new word")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationId = 1
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }

        //Remake Notif
        val imageWorker = OneTimeWorkRequestBuilder<RefreshWorkWorker>()
            .setInitialDelay(24, TimeUnit.HOURS)
            .addTag("Refresh Word Save")
            .build()
        // 2
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniqueWork(
            "Timed-Notif",
            ExistingWorkPolicy.REPLACE,
            imageWorker
        )

        return Result.success()
    }

    fun write_to_file(word_index:Int){
        val bufferedWriter =
            BufferedWriter(FileWriter(File(context?.filesDir.toString() + File.separator + "lastword.txt")))
        bufferedWriter.write(word_index.toString())
        bufferedWriter.close()
    }

    fun delete_index(){
        val file = File(context?.filesDir, "lastword.txt")
        file.delete()
    }

    fun read_index():Int{
        val bufferedReader: BufferedReader = File(context?.filesDir, "lastword.txt").bufferedReader()
        val index = bufferedReader.use { it.readText() }
        Log.d("test", index)
        return((index).toInt())
    }
}