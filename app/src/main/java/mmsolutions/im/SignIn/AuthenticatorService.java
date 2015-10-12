package mmsolutions.im.SignIn;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        AbstractAuthenticator authenticator = new AbstractAuthenticator(this);
        return authenticator.getIBinder();
    }
}
