package com.fleurey.android.androidremoteserver;

import com.fleurey.android.androidremotecontract.ClientMessageContract;
import com.fleurey.android.androidremotecontract.ServerMessageContract;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Fabien Fleurey
 * @version 1.0 5/19/13
 */
public class HandleClientThread extends Thread {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public HandleClientThread(final Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            handleMessages();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleMessages() throws AWTException, IOException {
        Robot robot = new Robot();
        String message;
        while (!ClientMessageContract.C_DISCONNECT.equals((message = in.readLine()))) {
            if (ClientMessageContract.EVENT.equals(message)) {
                int key = in.read();
                robot.keyPress(key);
                robot.keyRelease(key);
                sendMessage(ServerMessageContract.EVENT_RECEIVED);
            }
        }
        sendMessage(ServerMessageContract.CLIENT_DISCONNECTED);
    }

    private void sendMessage(String message) {
        out.println(message);
    }
}
