package com.example.chatproject.ui.login;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ChatMeassageModel {

    // region global var
    private static ChatMeassageModel chatMeassageModel;
    private Context context;
    List<ChatMeassage> meassages;
    // endregion


    //OnMessagesAddListener messageAddListener;

//    public void setMessageAddListener(OnMessagesAddListener messageAddListener) {
//        this.messageAddListener = messageAddListener;
//    }

//    public  interface OnMessagesAddListener{
//        void onMessageAdd();
//    }
    public static ChatMeassageModel get(Context context)
    {
        if(chatMeassageModel == null)
        {
            chatMeassageModel = new ChatMeassageModel(context);
        }
        return chatMeassageModel;
    }

    public ChatMeassageModel(Context context) {

        this.context = context;

        ChatMeassage msg1 = new ChatMeassage("hey. how ru? i hope so you're good. nanananananan jnfwgni iniejrvwe numvv.", System.currentTimeMillis(), ChatMeassage.Type.Sent, "user1@server.com");
        ChatMeassage msg2 = new ChatMeassage("hey", System.currentTimeMillis(), ChatMeassage.Type.Received, "user2@server.com");

        meassages = new ArrayList<>();
        /*meassages.add(msg1);
        meassages.add(msg2);
        meassages.add(msg1);
        meassages.add(msg2);
        meassages.add(msg1);
        meassages.add(msg2);
        meassages.add(msg1);
        meassages.add(msg2);*/
    }

    public List<ChatMeassage> getMeassages(){
        return meassages;
    }

    public void addMessage(ChatMeassage meassage){
        meassages.add(meassage);
        //messageAddListener.onMessageAdd();
    }
}
