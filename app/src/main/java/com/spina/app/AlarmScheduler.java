package com.spina.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public final class AlarmScheduler {
    private static final int REQUEST_CODE = 4815;

    private AlarmScheduler() {}

    public static void schedule(Context context) {
        cancel(context);

        int minutes = Math.max(1, SpinaPrefs.getMinutes(context));
        long interval = minutes * 60_000L;
        long first = SystemClock.elapsedRealtime() + interval;

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            PendingIntent pendingIntent = pendingIntent(context);
            alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    first,
                    interval,
                    pendingIntent
            );
        }
    }

    public static void cancel(Context context) {
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent(context));
        }
    }

    private static PendingIntent pendingIntent(Context context) {
        Intent intent = new Intent(context, TimerReceiver.class);
        return PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }
}
