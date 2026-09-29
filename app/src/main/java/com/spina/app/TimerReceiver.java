package com.spina.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!SpinaPrefs.isActive(context)) return;

        int mode = SpinaPrefs.getMode(context);
        if (mode == SpinaPrefs.MODE_TIMER || mode == SpinaPrefs.MODE_HYBRID) {
            NotificationHelper.show(
                    context,
                    "Spina",
                    "Пора на несколько секунд проверить осанку и расслабить плечи."
            );
        }
    }
}
