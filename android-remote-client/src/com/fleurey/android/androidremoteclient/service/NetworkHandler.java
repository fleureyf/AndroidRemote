package com.fleurey.android.androidremoteclient.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.fleurey.android.androidremoteclient.network.NetworkManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

public class NetworkHandler extends Handler {

    public static final String EXTRA_ADDRESS = NetworkHandler.class.getName() + ".EXTRA_ADDRESS";
    public static final String EXTRA_PORT = NetworkHandler.class.getName() + ".EXTRA_PORT";

    public static final int MSG_CONNECT = 0;
    public static final int MSG_DISCONNECT = 1;
    public static final int MSG_EVENT = 2;

    public static final int RESPONSE_SUCCESS = 0;
    public static final int RESPONSE_FAILURE = 1;

    private static final String TAG = NetworkHandler.class.getSimpleName();

    private NetworkManager networkManager;

    public NetworkHandler(Looper looper, NetworkManager networkManager) {
        super(looper);
        this.networkManager = networkManager;
    }

    @Override
    public void handleMessage(Message msg) {
        Message response = Message.obtain();
        response.what = msg.what;
        boolean success = false;
        switch (msg.what) {
            case MSG_CONNECT:
                if (msg.getData() != null) {
                    String address = msg.getData().getString(EXTRA_ADDRESS);
                    int port = msg.getData().getInt(EXTRA_PORT);
                    success = networkManager.connect(buildInetAdress(address), port);
                } else {
                    Log.e(TAG, "Can't connect, bundle data is null");
                }
                break;
            case MSG_DISCONNECT:
                success = networkManager.disconnect();
                getLooper().quit();
                break;
            case MSG_EVENT:
                success = networkManager.sendEvent(msg.arg1);
                break;
        }
        if (success) {
            response.arg1 = RESPONSE_SUCCESS;
        } else {
            response.arg1 = RESPONSE_FAILURE;
        }
        try {
            msg.replyTo.send(response);
        } catch (RemoteException e) {
            Log.e(TAG, "Can't send response", e);
        }
    }

    private InetAddress buildInetAdress(String address) {
        if (!Pattern.matches("(\\d{1,3}\\.){3}\\d{1,3}", address)) {
            Log.e(TAG, "The address does not follow the IP pattern");
            return null;
        }
        String[] ip = Pattern.compile("\\.").split(address);
        byte[] ipInByte = new byte[]{(byte) Integer.parseInt(ip[0]), (byte) Integer.parseInt(ip[1]), (byte) Integer.parseInt(ip[2]), (byte) Integer.parseInt(ip[3])};
        try {
            return InetAddress.getByAddress(ipInByte);
        } catch (UnknownHostException e) {
            Log.e(TAG, "Can't build InetAddress", e);
            return null;
        }
    }
}