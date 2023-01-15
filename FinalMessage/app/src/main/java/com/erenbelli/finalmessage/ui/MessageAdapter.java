package com.erenbelli.finalmessage.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erenbelli.finalmessage.MessageModel;
import com.erenbelli.finalmessage.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    List<MessageModel> messageModelList;

    public MessageAdapter(List<MessageModel> messageModelList){
        this.messageModelList = messageModelList;
    }


    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        holder.setData(messageModel);
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView msgName, msg;
        public MessageViewHolder(View itemView) {
            super(itemView);
            msgName = itemView.findViewById(R.id.textViewMessageNameItem);
            msg = itemView.findViewById(R.id.textViewMessageItem);
        }
    public void setData(MessageModel messageModel){
            msgName.setText(messageModel.getMsgName());
            msg.setText(messageModel.getMsg());
    }

        }
    }

