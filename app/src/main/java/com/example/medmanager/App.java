package com.example.medmanager;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

//Custom Application class that runs when the application is started
public class App extends Application {

  // A unique identifier for the notification channel
  public static final String CHANNEL_ID = "channel";
  //onCreate method called when the application is created
  @Override
  public void onCreate() {
    super.onCreate();

    //Method that creates the notification channel
    createNotificationChannels();
  }
  //Method that creates the notification channel
  private void createNotificationChannels() {
    //If Android version is 8.0 (Oreo) or higher
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      //Create a new notification channel
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "MedManager notifications", NotificationManager.IMPORTANCE_HIGH);
      channel.setDescription("Medmanager notifications appear here");  // Kanalın açıklaması

      // Getting notification manager
      NotificationManager manager = getSystemService(NotificationManager.class);

      // Add notification channel to notification manager
      manager.createNotificationChannel(channel);
    }
  }
}
