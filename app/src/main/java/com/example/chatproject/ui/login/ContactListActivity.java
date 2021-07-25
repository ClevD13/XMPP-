package com.example.chatproject.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatproject.R;

public class ContactListActivity extends AppCompatActivity implements ContactListAdapter.OnItemClickListener {

    private RecyclerView contactList;
    private static final String LOGTAG = "ContactListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
 /*       fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* Snackbar.make(v,"Replace withyour own action", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();*//*
                addCOntact();
            }
        });*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact();
                Snackbar.make(view, "Replace withyour own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        contactList = (RecyclerView) findViewById(R.id.contact_list);
        contactList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        ContactListAdapter mAdapter = new ContactListAdapter(getApplicationContext());
        mAdapter.setOnItemClickListener(this);
        contactList.setAdapter(mAdapter);
    }


    private void addContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_contact_label_text);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.add_contact_Confirm_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOGTAG, "User clicked on OK");

            }
        });

        builder.setNegativeButton(R.string.add_contact_cancel_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOGTAG, "User clicked on Cancel");
                dialog.cancel();

            }
        });
        builder.show();
    }

    @Override
    public void OnitemClicked(String contactJid) {
        Intent i = new Intent(this, Chat_view.class);
        startActivity(i);
    }
}