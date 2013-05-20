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
public class ConnectionManager {

    private static final String TAG = ConnectionManager.class.getSimpleName();

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public void connect() {
        try {
            InetAddress serverAddress = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, (byte) 0, (byte) 20});
            socket = new Socket(serverAddress, 5566);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendEvent(EventEnum event) {
        if (out != null) {
            out.println(ClientMessageContract.EVENT);
            out.print(event);
        } else {
            Log.e(TAG, "Can't send event: out stream is null");
        }
    }

    public void disconnect() {
        if (out != null) {
            out.println(ClientMessageContract.C_DISCONNECT);
        }
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
