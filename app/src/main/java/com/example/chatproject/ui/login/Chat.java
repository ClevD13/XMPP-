package com.example.chatproject.ui.login;

public class Chat {
    private String jid;

    private String lastMEassage;

    public Chat(String jid, String lastMEassage) {
        this.jid = jid;
        this.lastMEassage = lastMEassage;
    }

    public String getJid() {
        return jid;
    }

    public String getLastMEassage() {
        return lastMEassage;
    }
}
