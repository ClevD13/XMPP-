package com.example.chatproject.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.ping.android.ServerPingWithAlarmManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.io.PipedReader;

public class Xmpp implements ConnectionListener {

    private static final String LOGTAG = "XMPPConnection";
    private final Context applicationContext;
    private String username;
    private String password;
    private String serviceName;
    private XMPPTCPConnection connection;
    private ConnectionState connectionState;
    private PingManager pingManager;
    private ChatManager chatManager;

    public Xmpp(Context applicationContext){
        Log.d(LOGTAG, "XMPP constructor called");
        this.applicationContext = applicationContext;
    }

    public static enum ConnectionState
    {
        OFFLINE, CONNECTING, ONLINE
    }


    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }


    public  void updateActivitiesOfConnectionStateChange( ConnectionState connectionState){
        ConnectionState connectionState1 = connectionState;
        String status;
        switch (connectionState)
        {
            case OFFLINE:
                status = "Offline";
                break;
            case CONNECTING:
                status = "Connecting.....";
            break;
            case ONLINE:
                status = "Online";
            break;
            default:
                status = "Offline";
                break;
        }
        Intent i = new Intent(Constants.BroadCastMessage.UI_CONNECTION_STATUS_CHANGE_FLAG);
        i.putExtra(Constants.UI_CONNECTION_STATUS_CHANGE, status);
        i.setPackage(applicationContext.getPackageName());
        applicationContext.sendBroadcast(i);
    }
    public void connect() throws IOException, XMPPException, SmackException
    {
        connectionState = ConnectionState.CONNECTING;
        updateActivitiesOfConnectionStateChange(ConnectionState.CONNECTING);
        gatherCrdential();
        XMPPTCPConnectionConfiguration conf = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(serviceName)
                .setHost(serviceName)
                //.setResource("ChatProject")
                .setKeystoreType(null)
                .setSendPresence(true)
                .setDebuggerEnabled(true)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
                .setCompressionEnabled(true).build();

        SmackConfiguration.DEBUG = true;
        XMPPTCPConnection.setUseStreamManagementDefault(true);

        connection = new XMPPTCPConnection(conf);
        connection.setUseStreamManagement(true);
        connection.setUseStreamManagementResumption(true);
        connection.setPreferredResumptionTime(5);
        connection.addConnectionListener(this);

        chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addIncomingListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {

                Log.d(LOGTAG, "message.getbody(): "+message.getBody());
                Log.d(LOGTAG, "message.getForm(): "+message.getFrom());

                String messageSource = message.getFrom().toString();

                String contactJid = "";
                if (messageSource.contains("/"))
                {
                    contactJid = messageSource.split("/")[0];
                    Log.d(LOGTAG, "The real jid is: "+contactJid);
                    Log.d(LOGTAG, "The message is from: "+from);
                }else
                {
                    contactJid = messageSource;
                }

                ChatMeassageModel.get(applicationContext).addMessage(new ChatMeassage(message.getBody(), System.currentTimeMillis(), ChatMeassage.Type.Received, contactJid));
                Intent intent = new Intent(Constants.BroadCastMessage.UI_NEW_MESSAGE_FLAG);
                intent.setPackage(applicationContext.getPackageName());
                applicationContext.sendBroadcast(intent);
            }
        });


        ServerPingWithAlarmManager.getInstanceFor(connection).setEnabled(true);
        pingManager = PingManager.getInstanceFor(connection);
        pingManager.setPingInterval(30);

        try{
            Log.d(LOGTAG, "Calling connect()");
            connection.connect();
            connection.login(username,password);
            Log.d(LOGTAG,"Login called");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void disconnect(){
        Log.d(LOGTAG, "Disconnected from server" + serviceName);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        prefs.edit().putBoolean("xmpp_logged_in",false).commit();

        if(connection != null)
        {
            connection.disconnect();
        }
    }

    public void sendMessage (String body, String toJid){
        Log.d(LOGTAG, "Sending message to: "+ toJid);

        EntityBareJid jid = null;

        try{
            jid = JidCreate.entityBareFrom(toJid);
        }catch (XmppStringprepException e){
            e.printStackTrace();
        }

//        Chat chat = chatManager.chatWith(jid);
        Chat chat = chatManager.chatWith(jid);
        try{
            Message message = new Message(jid, Message.Type.chat);
            message.setBody(body);
            chat.send(message);
            ChatMeassageModel.get(applicationContext).addMessage(new ChatMeassage(body, System.currentTimeMillis(), ChatMeassage.Type.Sent, toJid ));
        }catch (SmackException.NotConnectedException e){
            e.printStackTrace();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    private void gatherCrdential(){
        String jid = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                .getString("xmpp_jid", null);
        password = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                .getString("xmpp_password", null);

        if(jid != null)
        {
            username = jid.split("@")[0];
            serviceName = jid.split("@")[1];
        }else
        {
            username = "";
            serviceName = "";
        }
    }

    @Override
    public void connected(XMPPConnection connection) {
        Log.d(LOGTAG, "Connected");
        connectionState = ConnectionState.ONLINE;
        updateActivitiesOfConnectionStateChange(ConnectionState.ONLINE);
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        connectionState = ConnectionState.ONLINE;
        updateActivitiesOfConnectionStateChange(ConnectionState.ONLINE);
        Log.d(LOGTAG, "Authenticated");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        prefs.edit().putBoolean("xmpp_logged_in",true).commit();

        Intent i = new Intent(Constants.BroadCastMessage.UI_AUTHENTICATED);
        i.setPackage(applicationContext.getPackageName());
        applicationContext.sendBroadcast(i);
        Log.d(LOGTAG,"Sent the broadcast that we are authenticated");
    }

    @Override
    public void connectionClosed() {
        Log.d(LOGTAG, "connectionClosed");
        connectionState = ConnectionState.OFFLINE;
        updateActivitiesOfConnectionStateChange(ConnectionState.OFFLINE);
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d(LOGTAG, "connectionClosedOnError");
        connectionState = ConnectionState.OFFLINE;
        updateActivitiesOfConnectionStateChange(ConnectionState.OFFLINE);
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d(LOGTAG, "Reconnection Successful");
        connectionState = ConnectionState.ONLINE;
        updateActivitiesOfConnectionStateChange(ConnectionState.ONLINE);
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d(LOGTAG, "Reconnecting in " + seconds + "seconds");
        connectionState = ConnectionState.CONNECTING;
        updateActivitiesOfConnectionStateChange(ConnectionState.CONNECTING);
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d(LOGTAG, "Reconnection Failed");
        connectionState = ConnectionState.OFFLINE;
        updateActivitiesOfConnectionStateChange(ConnectionState.OFFLINE);
    }
}
