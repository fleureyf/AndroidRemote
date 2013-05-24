package com.fleurey.android.androidremoteclient.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.fleurey.android.androidremoteclient.R;

public class TestActivity extends NetworkActivity {

    private static final String TAG = TestActivity.class.getSimpleName();

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
                connect();
            }
        });
        btnSendEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEvent('a');
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        Log.d(TAG, "keycode: " + keyCode + ", unicode: " + event.getUnicodeChar() + ", asciicode: " + toAscii(event));
        sendEvent(toAscii(event));
        return true;
    }

    public static int toAscii(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            return keyCode - KeyEvent.KEYCODE_A + 'A';
        }
        return keyEvent.getUnicodeChar();
    }
}
