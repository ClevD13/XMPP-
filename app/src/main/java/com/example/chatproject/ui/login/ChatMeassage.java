package com.example.chatproject.ui.login;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

public class ChatMeassage {
    private  String message;
    private long timeStamp;
    private Type type;
    private String contactJid;

    public ChatMeassage(String message, long timeStamp, Type type, String contactJid) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.type = type;
        this.contactJid = contactJid;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Type getType() {
        return type;
    }

    public String getContactJid() {
        return contactJid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setContactJid(String contactJid) {
        this.contactJid = contactJid;
    }

    public String getFormattedTime(){
        long oneDayInMillis = TimeUnit.DAYS.toMillis(1);
        long timeDifference = System.currentTimeMillis() - timeStamp;
        return timeDifference < oneDayInMillis
                ? android.text.format.DateFormat.format("hh:mm a", timeStamp).toString()
                : android.text.format.DateFormat.format("dd MM - hh:mm a", timeStamp).toString();

    }

    public enum Type {
        Sent, Received
    }
}
