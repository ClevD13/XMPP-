package com.example.chatproject.ui.login;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ContactModel {
    private static final String LOGTAG = "ContactModel";
    private static ContactModel contactModel;
    private Context context;
    private List<Contacts> contacts;

    public static ContactModel get(Context context){
        if(contactModel == null)
        {
            contactModel = new ContactModel(context);
        }
        return contactModel;
    }

    private ContactModel(Context context1){
        context = context1;
        contacts = new ArrayList<>();

        Contacts contacts1 = new Contacts("contact@server.com",Contacts.SubscriptionType.NONE_NONE);
        contacts.add(contacts1);
        contacts.add(contacts1);
        contacts.add(contacts1);contacts.add(contacts1);
        contacts.add(contacts1);contacts.add(contacts1);
        contacts.add(contacts1);contacts.add(contacts1);
        contacts.add(contacts1);contacts.add(contacts1);

    }

    public List<Contacts> getContacts()
    {
        return contacts;
    }

    public void addContact(Contacts c){
        contacts.add(c);
    }
}
