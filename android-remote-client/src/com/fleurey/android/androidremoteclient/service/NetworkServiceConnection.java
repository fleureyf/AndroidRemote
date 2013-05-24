package com.fleurey.android.androidremoteclient.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;

/**
 * @author Fabien Fleurey
 * @version 1.0 5/23/13
 */
public class NetworkServiceConnection implements ServiceConnection {

    public interface NetworkServiceListener {
        public void onServiceBound(Messenger networkMessenger);
    }

    private NetworkServiceListener listener;
    private boolean bound = false;

    public NetworkServiceConnection(NetworkServiceListener listener) {
        this.listener = listener;
    }

    public boolean isBound() {
        return bound;
    }

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        bound = true;
        listener.onServiceBound(new Messenger(iBinder));
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        bound = false;
    }
}
