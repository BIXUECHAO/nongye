package com.szitu.cyjl.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.szitu.cyjl.JavaBean.Message;
import com.szitu.cyjl.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message_Adapter extends ArrayAdapter<Message> {
    private int layoutID;
    public Message_Adapter(@NonNull Context context, int resource, @NonNull List<Message> objects) {
        super(context, resource, objects);
        layoutID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(layoutID,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.civ = (CircleImageView)view.findViewById(R.id.m_icon);
            viewHolder.name = (TextView)view.findViewById(R.id.m_name);
            viewHolder.message = (TextView)view.findViewById(R.id.m_message);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with(getContext()).load(message.getIcon()).placeholder(R.drawable.headphoto).into(viewHolder.civ);
        viewHolder.name.setText(message.getName());
        viewHolder.message.setText(message.getMessage());
        return view;
    }
    class ViewHolder{
        CircleImageView civ;
        TextView name;
        TextView message;
    }
}
