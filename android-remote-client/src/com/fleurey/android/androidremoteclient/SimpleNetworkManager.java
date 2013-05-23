package com.fleurey.android.androidremoteclient;

import android.util.Log;
import com.fleurey.android.androidremotecontract.ClientMessageContract;
import com.fleurey.android.androidremotecontract.EventEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Fabien Fleurey
 * @version 1.0 5/20/13
 */
public class SimpleNetworkManager implements NetworkManager {

    private static final String TAG = SimpleNetworkManager.class.getSimpleName();

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    @Override
    public boolean connect(InetAddress address, int port) {
        try {
            socket = new Socket(address, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            return true;
        } catch (UnknownHostException e) {
            Log.e(TAG, "Connection error", e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Connection error", e);
            return false;
        }
    }

    @Override
    public boolean sendEvent(int keyCode) {
        if (out != null) {
            out.println(ClientMessageContract.EVENT);
            out.print(keyCode);
            return true;
        } else {
            Log.e(TAG, "Can't send event: out stream is null");
            return false;
        }
    }

    @Override
    public boolean disconnect() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.println(ClientMessageContract.C_DISCONNECT);
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Disconnect error", e);
            return false;
        }
    }
}
