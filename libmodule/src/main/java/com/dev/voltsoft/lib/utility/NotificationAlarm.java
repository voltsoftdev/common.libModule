package com.dev.voltsoft.lib.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.dev.voltsoft.lib.R;

public class NotificationAlarm {

    private static NotificationAlarm mInstance;

    public NotificationAlarm getInstance() {
        if (mInstance == null) {
            mInstance = new NotificationAlarm();
        }
        return mInstance;
    }

    public void notifyAlarm(Context context , Class activity , String title , String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, activity), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(context);
        mBuilder.setSmallIcon(R.drawable.kakaoaccount_icon);
        mBuilder.setTicker("Notification.Builder");
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setNumber(10);
        mBuilder.setContentTitle("Notification.Builder Title");
        mBuilder.setContentText("Notification.Builder Massage");
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            mBuilder.setPriority(Notification.PRIORITY_MAX);

            notificationManager.notify(111, mBuilder.build());
        } else {
            notificationManager.notify(111, mBuilder.getNotification());
        }
    }
}
