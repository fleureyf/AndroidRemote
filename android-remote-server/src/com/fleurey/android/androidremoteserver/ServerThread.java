package com.fleurey.android.androidremoteserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Fabien Fleurey
 * @version 1.0 5/19/13
 */
public class ServerThread {

    private static final int PORT = 5566;

    public static void main(String... args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                System.out.println("Listening on port " + PORT);
                Socket clientSocket = serverSocket.accept();
                new HandleClientThread(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
