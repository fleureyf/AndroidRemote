package com.fleurey.android.androidremoteclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;

/**
 * @author Fabien Fleurey
 * @version 1.0 5/23/13
 */
public class NetworkActivity extends Activity implements NetworkServiceConnection.NetworkServiceConnectionCallback {

    private static final String TAG = NetworkActivity.class.getSimpleName();
    private static final String ADDRESS = "192.168.0.20";
    private static final int PORT = 5555;

    private NetworkServiceConnection networkServiceConnection = new NetworkServiceConnection();
    private Messenger networkMessenger;

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(getApplicationContext(), ConnectionService.class), networkServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (networkServiceConnection.isBound()) {
            unbindService(networkServiceConnection);
            networkServiceConnection.setBound(false);
        }
    }

    @Override
    public void onServiceBound(Messenger networkMessenger) {
        this.networkMessenger = networkMessenger;
    }

    protected boolean connect() {
        if (!networkServiceConnection.isBound()) {
            Log.e(TAG, "Can't connect, network service is not bound");
            return false;
        }
        try {
            Message message = Message.obtain(null, NetworkHandler.MSG_DISCONNECT);
            Bundle data = new Bundle();
            data.putString(NetworkHandler.EXTRA_ADDRESS, ADDRESS);
            data.putInt(NetworkHandler.EXTRA_PORT, PORT);
            message.setData(data);
            networkServiceConnection.getNetworkMessager().send(message);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "connect error", e);
            return false;
        }
    }

    protected boolean sendEvent(int keyCode) {
        if (!networkServiceConnection.isBound()) {
            Log.e(TAG, "Can't send event, network service is not bound");
            return false;
        }
        try {
            networkServiceConnection.getNetworkMessager().send(Message.obtain(null, NetworkHandler.MSG_EVENT, keyCode, 0));
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "send event error", e);
            return false;
        }
    }

    protected boolean disconnect() {
        if (!networkServiceConnection.isBound()) {
            Log.e(TAG, "Can't disconnect, network service is not bound");
            return false;
        }
        try {
            networkServiceConnection.getNetworkMessager().send(Message.obtain(null, NetworkHandler.MSG_DISCONNECT));
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "disconnect error", e);
            return false;
        }
    }
}
