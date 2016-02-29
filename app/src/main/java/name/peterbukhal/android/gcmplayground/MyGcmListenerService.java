package name.peterbukhal.android.gcmplayground;

import android.content.Intent;
import android.os.Bundle;
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

        sendBroadcast(intent);

        Log.d(TAG, "onMessageReceived->\nfrom: " + from + "\ndata:" + data);
    }

    @Override
    public void onDeletedMessages() {
        Log.d(TAG, "onDeletedMessages");
    }

}
