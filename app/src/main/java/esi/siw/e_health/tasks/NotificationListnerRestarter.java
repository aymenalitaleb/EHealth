package esi.siw.e_health.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationListnerRestarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive", "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, ListenNotification.class));;
    }
}
