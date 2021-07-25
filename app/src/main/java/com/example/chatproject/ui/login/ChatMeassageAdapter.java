package com.example.chatproject.ui.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatproject.R;

import org.w3c.dom.Text;

import java.util.List;

public class ChatMeassageAdapter extends  RecyclerView.Adapter<ChatMeassageViewHolder> {

    private static final int SENT = 1;
    private static final int RECEIVED = 2;
    private static final String LOGTAG = "ChatMeassageAdapter";

    private List<ChatMeassage> chatMessageList;
    private LayoutInflater layoutInflater;
    private Context context;
    private String contactJid;
    private  OninformRecycleViewToScrollDownListener moninformRecycleViewToScrollDownListener;

    public void setMoninformRecycleViewToScrollDownListener(OninformRecycleViewToScrollDownListener moninformRecycleViewToScrollDownListener) {
        this.moninformRecycleViewToScrollDownListener = moninformRecycleViewToScrollDownListener;
    }

    public interface OninformRecycleViewToScrollDownListener {
        public void oninformRecycleViewToScrollDownListener(int size);
    }
    public ChatMeassageAdapter(Context context, String contactJid)
    {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.contactJid = contactJid;
        chatMessageList = ChatMeassageModel.get(context).getMeassages();

       // ChatMeassageModel.get(context).setMessageAddListener(this);
    }

    public void informRecycleViewToScrollDownListener(){
        moninformRecycleViewToScrollDownListener.oninformRecycleViewToScrollDownListener(chatMessageList.size());
    }

    @NonNull
    @Override
    public ChatMeassageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType)
        {
            case SENT:
                    itemView = layoutInflater.inflate(R.layout.chat_meassge_sent,parent,false);
                    return new ChatMeassageViewHolder(itemView);
            case RECEIVED:
                     itemView = layoutInflater.inflate(R.layout.chat_meassge_receive,parent,false);
                        return new ChatMeassageViewHolder(itemView);
            default:
                     itemView = layoutInflater.inflate(R.layout.chat_meassge_sent,parent,false);
                     return new ChatMeassageViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMeassageViewHolder holder, int position) {
        ChatMeassage chatMeassage = chatMessageList.get(position);
        holder.bindChat(chatMeassage);
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMeassage.Type msgType = chatMessageList.get(position).getType();
        if(msgType == ChatMeassage.Type.Sent)
        {
            return SENT;
        }
        else{
            return RECEIVED;
        }
    }

   // @Override
    public void onMessageAdd() {
        chatMessageList = ChatMeassageModel.get(context).getMeassages();
        notifyDataSetChanged();
        informRecycleViewToScrollDownListener();
    }
}

class ChatMeassageViewHolder extends RecyclerView.ViewHolder{
    private TextView mMesagebody, mMessageTimeStamp;
    private ImageView profileImage;

    public ChatMeassageViewHolder(View itemView){
        super(itemView);
        mMesagebody = (TextView) itemView.findViewById(R.id.text_body);
        mMessageTimeStamp = (TextView) itemView.findViewById(R.id.msg_time);
        profileImage = (ImageView) itemView.findViewById(R.id.profile);
    }

    public void bindChat(ChatMeassage chatMeassage){
        mMesagebody.setText(chatMeassage.getMessage());
        mMessageTimeStamp.setText(chatMeassage.getFormattedTime());
    }
}
