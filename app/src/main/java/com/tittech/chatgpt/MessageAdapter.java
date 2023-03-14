package com.tittech.chatgpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViiewHolder>{
    List<Messege> messegeList;

    public MessageAdapter(List<Messege> messegeList) {
        this.messegeList = messegeList;
    }

    @NonNull
    @Override
    public MyViiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
        MyViiewHolder myViiewHolder = new MyViiewHolder(chatView);
        return myViiewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViiewHolder holder, int position) {

        Messege messege = messegeList.get(position);
        if (messege.getSentBy().equals(Messege.SENT_BY_ME)){
            holder.left_chat_view.setVisibility(View.GONE);
            holder.right_chat_view.setVisibility(View.VISIBLE);
            holder.right_chat_text_view.setText(messege.getMessage());
        }else {
            holder.right_chat_view.setVisibility(View.GONE);
            holder.left_chat_view.setVisibility(View.VISIBLE);
            holder.left_chat_text_view.setText(messege.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messegeList.size();
    }

    public class MyViiewHolder extends RecyclerView.ViewHolder{
        LinearLayout left_chat_view,right_chat_view;
        TextView left_chat_text_view,right_chat_text_view;

        public MyViiewHolder(@NonNull View itemView) {
            super(itemView);
            left_chat_view = itemView.findViewById(R.id.left_chat_view);
            left_chat_text_view = itemView.findViewById(R.id.left_chat_text_view);
            right_chat_view = itemView.findViewById(R.id.right_chat_view);
            right_chat_text_view = itemView.findViewById(R.id.right_chat_text_view);
        }
    }
}
