package com.example.chatproject.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.chatproject.R;

public class Chat_view extends AppCompatActivity implements ChatMeassageAdapter.OninformRecycleViewToScrollDownListener {


    RecyclerView chatMssg;
    private EditText textSend;
    private ImageButton sendMessage;
    private String counterpartJid;
    private BroadcastReceiver broadcastReceiver;
    ChatMeassageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        Intent intent = getIntent();
        counterpartJid = intent.getStringExtra("contact_jid");
        setTitle(counterpartJid);

        chatMssg = (RecyclerView) findViewById(R.id.chatMsg);
        chatMssg.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        adapter = new ChatMeassageAdapter(getApplicationContext(), "user1@server.com");
        adapter.setMoninformRecycleViewToScrollDownListener(this);
        chatMssg.setAdapter(adapter);

        textSend = (EditText) findViewById(R.id.textInput);
        sendMessage = (ImageButton) findViewById(R.id.textSendButton);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ChatMeassageModel.get(getApplicationContext()).addMessage(new ChatMeassage(textSend.getText().toString(), System.currentTimeMillis(), ChatMeassage.Type.Sent, "user1@server.com"));
                XmppConnectionService.getConnection().sendMessage(textSend.getText().toString(), counterpartJid);

                textSend.getText().clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.receive_message) {
            ChatMeassageModel.get(getApplicationContext()).addMessage(new ChatMeassage("This is the the message received from a stranger", System.currentTimeMillis(), ChatMeassage.Type.Received, "user1@server.com"));
            textSend.getText().clear();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case Constants.BroadCastMessage.UI_NEW_MESSAGE_FLAG:
                        if (null != adapter)
                            adapter.onMessageAdd();
                        return;
                }
            }
        };
        IntentFilter filter = new IntentFilter(Constants.BroadCastMessage.UI_NEW_MESSAGE_FLAG);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void oninformRecycleViewToScrollDownListener(int size) {
        chatMssg.scrollToPosition(size - 1);
    }
}