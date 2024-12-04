package edu.utsa.cs3443.msaid;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.util.Log;

/**
 * AlarmReceiver is a BroadcastReceiver that is triggered when an alarm goes off.
 * It creates a notification to notify the user about the alarm.
 */
public class AlarmReceiver extends BroadcastReceiver {
    // Unique ID for the notification channel that is used to display the alarms
    private static final String CHANNEL_ID = "alarm_channel";
    // Tag that is used for logging errors and debugging information
    private static final String TAG = "AlarmReceiver";

    /**
     * Called when the alarm goes off and the BroadcastReceiver is triggered.
     * Creates and displays a notification if the app has been given the appropriate permission.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received, containing alarm information.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieves the alarm name passed with the intent
        String alarmName = intent.getStringExtra("alarm_name");

        // Creates the notification channel for sending notifications
        createNotificationChannel(context);

        // Checks if the app has permission to post notifications
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Notification permission not granted."); // Logs an error if permissions are missing
            return; // Exits if permissions were not granted
        }

        // Tries to build and display the notification
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_alert) // Sets the icon for the notification
                    .setContentTitle("Alarm Notification") // Sets the title for the notification
                    .setContentText("It's time for: " + alarmName) // Sets the content text for the notification
                    .setPriority(NotificationCompat.PRIORITY_HIGH) // Sets the priority for the notification
                    .setAutoCancel(true); // Dismisses the notification when clicked

            // Gets an instance for the notification manager and displays the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build()); // Sends the notification with a unique ID of (1)
        } catch (SecurityException e) {
            // Logs an error message if a SecurityException occurs
            Log.e(TAG, "Failed to send notification due to security exception", e);
        }
    }

    /**
     * Creates a notification channel
     * This is required for sending notifications on Modern Android APIs.
     *
     * @param context The Context in which the receiver is running.
     */
    private void createNotificationChannel(Context context) {
        // Creates the notification channel with a name, a description, and an importance level
        CharSequence name = "Alarm Channel"; // Name of the channel
        String description = "Channel for Alarm Notifications"; // Description of the channel
        int importance = NotificationManager.IMPORTANCE_HIGH; // Sets importance level to high
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description); // Sets the description of the channel

        // Registers the channel with the notification manager
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}