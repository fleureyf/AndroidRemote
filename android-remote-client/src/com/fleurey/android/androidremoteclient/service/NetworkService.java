package com.fleurey.android.androidremoteclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;
import com.fleurey.android.androidremoteclient.network.NetworkManager;
import com.fleurey.android.androidremoteclient.network.SimpleNetworkManager;

/**
 * @author Fabien Fleurey
 * @version 1.0 5/21/13
 */
public class NetworkService extends Service {

    private static final String TAG = NetworkService.class.getSimpleName();

    private NetworkManager networkManager = new SimpleNetworkManager();
    private NetworkHandler networkHandler;
    private Messenger networkMessenger;

    @Override
    public void onCreate() {
        Log.d(TAG, "__ON_CREATE__");
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("NetworkThread", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        networkHandler = new NetworkHandler(handlerThread.getLooper(), networkManager);
        networkMessenger = new Messenger(networkHandler);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "__ON_DESTROY__");
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return networkMessenger.getBinder();
    }
}
