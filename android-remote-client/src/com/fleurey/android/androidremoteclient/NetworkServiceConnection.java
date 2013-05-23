package com.fleurey.android.androidremoteclient;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;

/**
 * @author Fabien Fleurey
 * @version 1.0 5/23/13
 */
public class NetworkServiceConnection implements ServiceConnection {

    public interface NetworkServiceConnectionCallback {
        public void onServiceBound(Messenger networkMessenger);
    }

    private Messenger networkMessager;
    private boolean bound = false;

    public Messenger getNetworkMessager() {
        return networkMessager;
    }

    public boolean isBound() {
        return bound;
    }

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        networkMessager = new Messenger(iBinder);
        bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        bound = false;
    }
}
