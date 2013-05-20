package com.fleurey.android.androidremoteclient;

import android.os.*;
import android.os.Process;
import com.fleurey.android.androidremotecontract.EventEnum;

/**
 * @author Fabien Fleurey
 * @version 1.0 5/20/13
 */
public class NetworkThread extends HandlerThread {

    public static final int MSG_CONNECT = 0;
    public static final int MSG_DISCONNECT = 1;
    public static final int MSG_EVENT = 2;

    private ConnectionManager connectionManager = new ConnectionManager();
    private Handler handler;

    public NetworkThread() {
        super("NetworkThread", Process.THREAD_PRIORITY_BACKGROUND);
        start();
        handler = new NetworkThreadHandler(getLooper());
    }

    public Handler getHandler() {
        return handler;
    }

    private class NetworkThreadHandler extends Handler {

        public NetworkThreadHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONNECT:
                    connectionManager.connect();
                    break;
                case MSG_DISCONNECT:
                    connectionManager.disconnect();
                    getLooper().quit();
                    break;
                case MSG_EVENT:
                    connectionManager.sendEvent((EventEnum) msg.obj);
                    break;
            }
        }
    }

}
