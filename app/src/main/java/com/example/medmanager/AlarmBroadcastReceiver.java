package com.example.medmanager;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.medmanager.App.CHANNEL_ID;

// AlarmBroadcastReceiver class derived from BroadcastReceiver class
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private NotificationManagerCompat notificationManager;
    // Variable used to keep track of notification number
    public static int NOTIFICATION_NUMBER = 1;

    //The onReceive method of the BroadcastReceiver class is called when the alarm is triggered
    @Override
    public void onReceive(Context context, Intent intent) {
        // Getting drug and user information from Intent
        String medName = intent.getStringExtra("medName");
        String medQty = intent.getStringExtra("medQty");

        // Playing alarm sound using MediaPlayer (this part is currently in the comment line)
        MediaPlayer player = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        player.start();
        //Create notification manager and send notification
        notificationManager = NotificationManagerCompat.from(context);
        sendOnChannel(context, "Please take " + medQty + " " + medName + " take your medicine ", intent);
    }
    //Method to send the notification to the specified channel
    public void sendOnChannel(Context context, String message, Intent intent) {
        //Create notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo1)
                .setContentTitle("Medication Reminder")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .build();

        // Send notification

        notificationManager.notify(NOTIFICATION_NUMBER++, notification);
    }
}
