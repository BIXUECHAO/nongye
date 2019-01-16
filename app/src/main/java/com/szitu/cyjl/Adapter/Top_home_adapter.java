package com.szitu.cyjl.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.szitu.cyjl.JavaBean.Top_list_bean;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.MyNew;

import java.util.ArrayList;
import java.util.List;


public class Top_home_adapter extends ArrayAdapter<MyNew>{
    int layoutId;

    public Top_home_adapter(@NonNull Context context, int resource, @NonNull List<MyNew> objects) {
        super(context, resource, objects);
        layoutId = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyNew myNew = getItem(position);
        View view ;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(layoutId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.top_home_Image);
            viewHolder.title = (TextView)view.findViewById(R.id.top_home_title);
            viewHolder.content = (TextView) view.findViewById(R.id.top_home_content);
            viewHolder.date = (TextView) view.findViewById(R.id.top_home_date);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        Glide.with(getContext()).load(myNew.getUrl()).placeholder(R.drawable.logo).into(viewHolder.imageView);
        viewHolder.title.setText(myNew.getName());
        viewHolder.content.setText(myNew.getContent());
        viewHolder.date.setText(myNew.getDate());
        return view;
    }
    class ViewHolder{
        ImageView imageView;
        TextView title;
        TextView content;
        TextView date;
    }

}
