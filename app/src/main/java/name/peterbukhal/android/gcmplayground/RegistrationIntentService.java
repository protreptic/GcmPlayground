package name.peterbukhal.android.gcmplayground;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created on 29/02/16 18:53 by
 *
 * @author Peter Bukhal (petr@taxik.ru)
 */
public class RegistrationIntentService extends IntentService {

    public static final String TAG = "GcmRegistration";
    public static final String ACTION_REGISTRATION = "name.peterbukhal.android.gcmplayground.action.ACTION_REGISTRATION";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.server_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Intent intent1 = new Intent();
            intent1.setAction(ACTION_REGISTRATION);
            intent1.putExtra(MainActivity.TAG_TOKEN, token);

            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);

            Log.d(TAG, "Registration success! " + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
