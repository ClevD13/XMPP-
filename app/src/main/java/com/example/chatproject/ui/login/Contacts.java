package com.example.chatproject.ui.login;

import java.security.PublicKey;
import java.util.concurrent.Flow;

public class Contacts {
    private String jid;
    private SubscriptionType sub;

    public enum SubscriptionType{
        NONE_NONE,
        NONE_PENDING,
        NONE_TO,

        PENDING_NONE,
        PENDING_PENDING,
        PENDING_TO,

        FROM_NONE,
        FROM_PENDING,
        FROM_TO
    }

    public Contacts(String jid, SubscriptionType sub){
        this.jid = jid;
        this.sub = sub;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public SubscriptionType getSub() {
        return sub;
    }

    public void setSub(SubscriptionType sub) {
        this.sub = sub;
    }
}
