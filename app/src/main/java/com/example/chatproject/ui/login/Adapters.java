package com.example.chatproject.ui.login;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatproject.R;

import java.util.List;

public class Adapters extends RecyclerView.Adapter<ChatHolder> {

    public interface OnItemClickListenner{
        public void onItemClicked(String cotactJid);
    }
    public Adapters(Context context) {
        this.chatList = ChatModel.get(context).getChats();
    }
    private OnItemClickListenner  onItemClickListenner;

    public Adapters(OnItemClickListenner onItemClickListenner) {
        this.onItemClickListenner = onItemClickListenner;
    }

    public OnItemClickListenner getOnItemClickListenner() {
        return onItemClickListenner;
    }

    public void setOnItemClickListenner(OnItemClickListenner onItemClickListenner) {
        this.onItemClickListenner = onItemClickListenner;
    }

    List<Chat> chatList;
    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chat_list_item, parent, false);
        ChatHolder viewHolder = new ChatHolder(view, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.bindChat(chat);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}

class ChatHolder extends RecyclerView.ViewHolder{

    private static final String LOGTAG = "ChatHolder";
    private TextView contactView;
    private TextView message;
    private TextView timeStamp;
    private ImageView profileIamge;
    private Chat mChat;
    private Adapters adapters1;

    public ChatHolder(@NonNull View itemView, Adapters adapters) {
        super(itemView);

        contactView = (TextView) itemView.findViewById(R.id.user);
        message = (TextView) itemView.findViewById(R.id.text);
        timeStamp = (TextView) itemView.findViewById(R.id.time);
        adapters1 = adapters;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adapters.OnItemClickListenner listenner = adapters1.getOnItemClickListenner();
                if(listenner != null)
                {
                    listenner.onItemClicked(contactView.getText().toString());
                }
                Log.d(LOGTAG, "Clicked on the item in the recycleView");
            }
        });
    }

    public void bindChat(Chat chat)
    {
        contactView.setText(chat.getJid());
        message.setText(chat.getLastMEassage());
        timeStamp.setText("12:00");
    }

}

