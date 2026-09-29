package com.spina.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public final class NotificationHelper {
    public static final String CHANNEL_ID = "spina_reminders";

    private NotificationHelper() {}

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Напоминания Spina",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Напоминания о паузе и осанке");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void show(Context context, String title, String text) {
        createChannel(context);

        android.content.Intent intent =
                new android.content.Intent(context, MainActivity.class);
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);

        android.app.PendingIntent pendingIntent =
                android.app.PendingIntent.getActivity(
                        context,
                        100,
                        intent,
                        android.app.PendingIntent.FLAG_UPDATE_CURRENT |
                                android.app.PendingIntent.FLAG_IMMUTABLE
                );

        android.app.Notification.Builder builder =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? new android.app.Notification.Builder(context, CHANNEL_ID)
                        : new android.app.Notification.Builder(context);

        builder.setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(android.app.Notification.PRIORITY_DEFAULT);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (manager != null) {
            manager.notify((int) (System.currentTimeMillis() & 0x7fffffff), builder.build());
        }
    }
}
