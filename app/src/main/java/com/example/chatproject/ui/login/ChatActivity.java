package com.example.chatproject.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChatActivity extends AppCompatActivity implements Adapters.OnItemClickListenner{

    private RecyclerView chatsR1;
    private FloatingActionButton newConverSation;
    private static final String LOGTAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        boolean logged_in_state = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("xmpp_logged_in", false);
        if(!logged_in_state)
        {
            Log.d(LOGTAG, "Logged in state: "+ logged_in_state + "calling stopself()");
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        else{
            if(!Utilities.isServiceRunning(XmppConnectionService.class, getApplicationContext())){
                Log.d(LOGTAG, "Service not running, starting it ....");
                Intent i1 = new Intent(this, XmppConnectionService.class);
                startService(i1);
            }else{
                Log.d(LOGTAG, "Service is already running.");
            }
        }




        chatsR1 = (RecyclerView) findViewById(R.id.chat_r);
        chatsR1.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        Adapters mA = new Adapters(getApplicationContext());
        mA.setOnItemClickListenner(this);
        chatsR1.setAdapter(mA);

        newConverSation = (FloatingActionButton) findViewById(R.id.float_button);
        newConverSation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this, ContactListActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_me_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.me)
        {
            Intent i = new Intent (this, MeActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(String cotactJid) {
        Intent i = new Intent(this, Chat_view.class);
        i.putExtra("contact_jid", cotactJid);
        startActivity(i);
    }
}
