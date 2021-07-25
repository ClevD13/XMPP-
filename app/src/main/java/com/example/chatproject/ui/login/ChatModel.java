package com.example.chatproject.ui.login;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ChatModel {

    private static ChatModel sChatsModel;
    private Context mContext;

    public static ChatModel get(Context context)
    {
        if(sChatsModel == null)
        {
            sChatsModel = new ChatModel(context);
        }
        return sChatsModel;
    }

    public ChatModel(Context context) {
        mContext = context;
    }

    public List<Chat> getChats()
    {
       List<Chat> chats = new ArrayList<>();
       Chat chat1 = new Chat ("clev@xabber.org", "Hey there");
       chats.add(chat1);
        Chat chat2 = new Chat ("user1@server3.com", "Hey there");
        chats.add(chat2);
        Chat chat3 = new Chat ("user1@server4.com", "Hey there");
        chats.add(chat3);
        Chat chat4 = new Chat ("user1@server5.com", "Hey there");
        chats.add(chat4);
        Chat chat5 = new Chat ("user1@server6.com", "Hey there");
        chats.add(chat5);

        return  chats;
    }
}
