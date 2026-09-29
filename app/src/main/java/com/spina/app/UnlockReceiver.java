package com.spina.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UnlockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Intent.ACTION_USER_PRESENT.equals(intent.getAction())) return;
        if (!SpinaPrefs.isActive(context)) return;

        int mode = SpinaPrefs.getMode(context);
        if (mode == SpinaPrefs.MODE_STANDARD || mode == SpinaPrefs.MODE_HYBRID) {
            NotificationHelper.show(
                    context,
                    "Spina",
                    "Небольшая пауза: проверь положение спины и плеч."
            );
        }
    }
}
