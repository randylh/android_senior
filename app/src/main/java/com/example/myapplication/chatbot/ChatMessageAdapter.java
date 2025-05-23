package com.example.myapplication.chatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

//https://cloud.tencent.com/document/api/1729/105701  腾讯大模型请求

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (0 == viewType) {
            // User message layout
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_user, parent, false);
        }else{
            // AI message layout
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_ai, parent, false);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static abstract class MessageViewHolder extends RecyclerView.ViewHolder {

        protected final TextView messageText;
//        protected final LinearLayout messageBubble;
//        protected final ImageView messageImage;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
        }

    }
}
