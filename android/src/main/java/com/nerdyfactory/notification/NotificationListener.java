package com.nerdyfactory.notification;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.app.Notification;

import com.facebook.react.bridge.WritableNativeMap;

public class NotificationListener extends NotificationListenerService {
    private static final String TAG = "NotificationListener";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG, "Notification received: "+sbn.getPackageName()+":"+sbn.getNotification().tickerText);

        if (sbn.getNotification().tickerText == null) {
            return;
        }

        WritableNativeMap params = new WritableNativeMap();
        // params.putString("text", sbn.getNotification().tickerText.toString());

        String app = sbn.getPackageName();
        if (app.equals(NotificationModule.smsApp)) {
            params.putString("app", "sms");
        } else {
            params.putString("app", app);
        }
        Notification notification = sbn.getNotification();
        try {
            params.putString("title", notification.extras.get("android.title").toString());
        } catch (Exception e){
            params.putString("title", "");
        }
        try {
            params.putString("text", notification.extras.get("android.text").toString());
        } catch (Exception e){
            params.putString("text", "");
        }

        try {
            params.putString("tickerText", notification.tickerText.toString());
        } catch (Exception e){
            params.putString("tickerText", "");
        }

        NotificationModule.sendEvent("notificationReceived", params);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}
}
