package com.fleurey.android.androidremoteclient.network;

import com.fleurey.android.androidremotecontract.EventEnum;

import java.net.InetAddress;

/**
 * @author Fabien Fleurey
 * @version 1.0 5/21/13
 */
public interface NetworkManager {

    public boolean connect(InetAddress address, int port);
    public boolean sendEvent(int keyCode);
    public boolean disconnect();

}
