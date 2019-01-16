package com.szitu.cyjl.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.szitu.cyjl.JavaBean.Msg;
import com.szitu.cyjl.R;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> msgList;

    public MsgAdapter(List<Msg> msgs){
        msgList = msgs;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Msg msg = msgList.get(position);
        if(msg.getType() == Msg.TYPE_RECRIVED){
            holder.right_layout.setVisibility(View.GONE);
            holder.left_layout.setVisibility(View.VISIBLE);
            holder.left_msg.setText(msg.getContent());
        }else if(msg.getType() == Msg.TYPE_SENT){
            holder.left_layout.setVisibility(View.GONE);
            holder.right_layout.setVisibility(View.VISIBLE);
            holder.right_msg.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout left_layout,right_layout;
        TextView left_msg,right_msg;
        public ViewHolder(View itemView) {
            super(itemView);
            left_layout = (LinearLayout)itemView.findViewById(R.id.left_layout);
            right_layout = (LinearLayout)itemView.findViewById(R.id.right_layout);
            left_msg = (TextView)itemView.findViewById(R.id.left_msg);
            right_msg = (TextView)itemView.findViewById(R.id.right_msg);
        }
    }
}
