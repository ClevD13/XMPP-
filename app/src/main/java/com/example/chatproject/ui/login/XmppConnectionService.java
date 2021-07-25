package com.example.chatproject.ui.login;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.ping.android.ServerPingWithAlarmManager;

import java.io.IOException;

public class XmppConnectionService extends Service {

    public static final String LOGTAG = "XMPPConService";
    private boolean active;
    private Thread thread;
    private Handler handler;

    public static Xmpp getConnection() {
        return connection;
    }

    private static Xmpp connection;
    private String userName, password;

    public XmppConnectionService(){
    }



    private void initConnection()
    {
        Log.d(LOGTAG, "initConnection()");
        if(connection == null)
        {
            connection = new Xmpp(this);
        }

//        try{
//            connection.connect();
//        }catch (IOException| SmackException| XMPPException e)
//        {
//            Log.d(LOGTAG, "Something went wrong while connecting");
//        }
        try {
            connection.connect();
        } catch (IOException|SmackException| XMPPException e) {
            Log.d(LOGTAG, "Something went wrong while connecting");

            Intent i = new Intent(Constants.BroadCastMessage.UI_CONNECTION_ERROR);
            i.setPackage(getApplicationContext().getPackageName());
            getApplicationContext().sendBroadcast(i);
            Log.d(LOGTAG,"Sent the broadcast for connection error from service");
            e.printStackTrace();
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        ServerPingWithAlarmManager.onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        userName= intent.getStringExtra("xmpp_jid");
        password = intent.getStringExtra("xmpp_password");
        Log.d(LOGTAG,"jidPaswword");
        start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServerPingWithAlarmManager.onDestroy();
        stop();
    }

    public void start()
    {
        Log.d(LOGTAG, "Service Start() function called active: "+ active);

        if(!active)
        {
            active = true;
            if(thread == null || !thread.isAlive())
            {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        handler = new Handler();
                        initConnection();
                        Looper.loop();
                    }
                });
                thread.start();
            }
        }
    }

    public void stop()
    {
        Log.d(LOGTAG, "Stop()");
        active = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (connection != null){
                    connection.disconnect();
                }
            }
        });
    }
}
