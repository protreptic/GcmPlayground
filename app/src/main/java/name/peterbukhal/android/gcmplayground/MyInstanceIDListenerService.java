package name.peterbukhal.android.gcmplayground;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created on 29/02/16 19:24 by
 *
 * @author Peter Bukhal (petr@taxik.ru)
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        startService(new Intent(this, RegistrationIntentService.class));
    }

}
