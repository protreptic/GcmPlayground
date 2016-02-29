package name.peterbukhal.android.gcmplayground;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.server_sender_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            getSharedPreferences(getPackageName(), MODE_PRIVATE)
                    .edit()
                    .putString("token", token)
                    .commit();

            Toast.makeText(getApplicationContext(), "Registration success!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Client token = " + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
