package com.fleurey.android.androidremoteclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.fleurey.android.androidremotecontract.EventEnum;

public class MyActivity extends Activity {

    private NetworkThread networkThread = new NetworkThread();

    private Button btnConnect;
    private Button btnSendEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnConnect = (Button) findViewById(R.id.btn_connect);
        btnSendEvent = (Button) findViewById(R.id.btn_sendevent);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkThread.getHandler().obtainMessage(NetworkThread.MSG_CONNECT).sendToTarget();
            }
        });
        btnSendEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkThread.getHandler().obtainMessage(NetworkThread.MSG_EVENT, EventEnum.UP).sendToTarget();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkThread.getHandler().obtainMessage(NetworkThread.MSG_DISCONNECT).sendToTarget();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_quit) {

            return true;
        }
        return false;
    }
}
