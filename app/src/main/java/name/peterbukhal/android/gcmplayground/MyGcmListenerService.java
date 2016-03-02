package name.peterbukhal.android.gcmplayground;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created on 29/02/16 16:56 by
 *
 * @author Peter Bukhal (petr@taxik.ru)
 */
public class MyGcmListenerService extends GcmListenerService {

    public static final String TAG = "GcmListener";
    public static final String ACTION_NEW_MESSAGE = "name.peterbukhal.android.gcmplayground.action.ACTION_NEW_MESSAGE";

    @Override
    public void onMessageSent(String msgId) {
        Log.d(TAG, "onMessageSent->\nmsgId: " + msgId);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        MainActivity.Message message = new MainActivity.Message(data.getString("time"), data.getString("message"));

        Intent intent = new Intent();
        intent.setAction(ACTION_NEW_MESSAGE);
        intent.putExtra("message", message);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        notification(message);

        Log.d(TAG, "onMessageReceived->\nfrom: " + from + "\ndata:" + data);
    }

    @Override
    public void onDeletedMessages() {
        Log.d(TAG, "onDeletedMessages");
    }

    private static int notificationId = 4125182;

    private void notification(MainActivity.Message message) {
        NotificationManagerCompat.from(getApplicationContext()).cancelAll();

        Intent viewIntent1 = new Intent(getApplicationContext(), MainActivity.class);
        viewIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent viewPendingIntent1 = PendingIntent.getActivity(getApplicationContext(), 0, viewIntent1, 0);
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(viewPendingIntent1)
                .setContentText(message.getMessage())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSubText(message.getTime())
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(++notificationId, notification);
    }

}
