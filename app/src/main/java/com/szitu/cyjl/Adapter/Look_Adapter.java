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
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.Subscribe;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Look_Adapter extends ArrayAdapter<Subscribe> {
    private int layoutID;
    public Look_Adapter(@NonNull Context context, int resource, @NonNull List<Subscribe> objects) {
        super(context, resource, objects);
        layoutID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Subscribe subscribe = getItem(position);
        View view ;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(layoutID,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.look_icon = (ImageView)view.findViewById(R.id.look_icon_item);
            viewHolder.look_content = (TextView)view.findViewById(R.id.look_content_item);
            viewHolder.look_njname = (TextView)view.findViewById(R.id.look_njname_item);
            viewHolder.look_min = (TextView)view.findViewById(R.id.look_min_item);
            viewHolder.look_max = (TextView)view.findViewById(R.id.look_max_item);
            viewHolder.look_local = (TextView)view.findViewById(R.id.look_local_item);
            viewHolder.look_date = (TextView)view.findViewById(R.id.look_date_item);
            viewHolder.look_number = (TextView)view.findViewById(R.id.look_mannumber_item);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        List<String> lists = subscribe.getFilebyte();
        for(String l : lists){
            if(l != null){
                Glide.with(getContext()).load(l).placeholder(R.drawable.logo).into(viewHolder.look_icon);
                break;
            }
        }
        if(subscribe.getType().equals("农机")){
            viewHolder.look_number.setVisibility(View.GONE);
        }else if(subscribe.getType().equals("人员互助")){
            if(subscribe.getSp_number() == null){

            }else {
                viewHolder.look_number.setText(subscribe.getSp_number() + "人");
            }
        }else if(subscribe.getType().equals("农作物")){
            if(subscribe.getSp_number() == null){

            }else {
                viewHolder.look_number.setText(subscribe.getSp_number()+"千克");
            }
        }
        viewHolder.look_content.setText(subscribe.getIntroduce());
        viewHolder.look_njname.setText(subscribe.getSp_name());
        viewHolder.look_min.setText(subscribe.getMin());
        viewHolder.look_max.setText(subscribe.getMax());
        viewHolder.look_local.setText(subscribe.getLocal());
        viewHolder.look_date.setText(subscribe.getLocaltime());
        return view;
    }
    class ViewHolder{
        ImageView look_icon;
        TextView look_content,look_njname,look_min,look_max,look_local,look_date,look_number;
    }
}
