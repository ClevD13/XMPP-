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

public class ContactListAdapter extends RecyclerView.Adapter<ContactHolder>{

    public interface OnItemClickListener{
        public void OnitemClicked(String contactJid);
    }

    private OnItemClickListener onItemClickListener;
    private List<Contacts> contacts;
    private Context context;
    private static final String LOGTAG = "ContactListAdapter";

    public ContactListAdapter(Context context1)
    {
        contacts = ContactModel.get(context).getContacts();
        context = context1;
        Log.d(LOGTAG, "Constructor of ChatListAdapter, the size of the backing list is : "+contacts.size());

    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater
                .inflate(R.layout.contact_list_item, parent, false);
        return new ContactHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contacts contacts2 = contacts.get(position);
        holder.bindContact(contacts2);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}

class ContactHolder extends RecyclerView.ViewHolder{

    public TextView jidTextView;
    public TextView subs;
    private Contacts contacts;
    private ImageView profile;
    private ContactListAdapter adapter;
    private static final String LOGTAG = "ContactHolder";

    public ContactHolder(@NonNull View itemView, ContactListAdapter adapter1) {
        super(itemView);
        adapter = adapter1;
        jidTextView = (TextView) itemView.findViewById(R.id.contact_jid_string);
        subs = (TextView) itemView.findViewById(R.id.sub_type);
        profile = (ImageView) itemView.findViewById(R.id.profile_contact);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOGTAG, "User clicked on contact Item");
                ContactListAdapter.OnItemClickListener listener = adapter.getOnItemClickListener();
                if (listener != null)
                {
                    Log.d(LOGTAG, "Calling the listener method");
                    listener.OnitemClicked(jidTextView.getText().toString());
                }
            }
        });
    }

    void bindContact(Contacts c)
    {
        contacts = c;
        if(contacts == null){
            return;
        }
        jidTextView.setText((contacts.getJid()));
        subs.setText("NONE_NONE");
        //profile.setImageResource(R.drawable.i);

    }
}