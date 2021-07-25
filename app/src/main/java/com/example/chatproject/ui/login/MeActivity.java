package com.example.chatproject.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.example.chatproject.R;

public class MeActivity extends AppCompatActivity {

    private TextView connectionStatus;
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        String status;
        Xmpp xmpp = XmppConnectionService.getConnection();
        connectionStatus = (TextView) findViewById(R.id.connection_status);
        if(xmpp != null)
        {
            status = xmpp.getConnectionState().toString();
            connectionStatus.setText(status);
        }
        //connectionStatus = (TextView) findViewById(R.id.connection_status);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action)
                {
                    case Constants.BroadCastMessage.UI_CONNECTION_STATUS_CHANGE_FLAG:

                        String status = intent.getStringExtra(Constants.UI_CONNECTION_STATUS_CHANGE);
                        connectionStatus.setText(status);
                        break;
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BroadCastMessage.UI_CONNECTION_STATUS_CHANGE_FLAG);
        this.registerReceiver(broadcastReceiver, filter);
    }
}