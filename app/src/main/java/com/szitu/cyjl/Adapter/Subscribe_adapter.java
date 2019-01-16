package com.szitu.cyjl.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.szitu.cyjl.Activity.WelcomeActivity;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.Subscribe;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Subscribe_adapter extends ArrayAdapter<Subscribe> {
    private static final String TAG = "Subscribe_adapter";
    int layoutId;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public Subscribe_adapter(@NonNull Context context, int resource, @NonNull List<Subscribe> objects) {
        super(context, resource, objects);
        layoutId = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Subscribe subscribe = getItem(position);
        View view ;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(layoutId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.leixing = (TextView)view.findViewById(R.id.leixing);
            viewHolder.UserIcon =(CircleImageView) view.findViewById(R.id.user_icon);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.Image);
            viewHolder.title = (TextView)view.findViewById(R.id.title);
            viewHolder.content = (TextView) view.findViewById(R.id.content);
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.min = (TextView)view.findViewById(R.id.min);
            viewHolder.max = (TextView)view.findViewById(R.id.max);
            viewHolder.local = (TextView)view.findViewById(R.id.local);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        List<String> lists = subscribe.getFilebyte();
        for(String l : lists){
            if(l != null){
                Glide.with(getContext()).load(l).placeholder(R.drawable.logo).into(viewHolder.imageView);
                break;
            }
        }
        Glide.with(getContext()).load(subscribe.getUerIcon()).placeholder(R.drawable.headphoto).into(viewHolder.UserIcon);
        viewHolder.leixing.setText(subscribe.getType());
        viewHolder.title.setText(subscribe.getUsername());
        viewHolder.content.setText(subscribe.getIntroduce());
        viewHolder.min.setText(subscribe.getMin());
        viewHolder.max.setText(subscribe.getMax());
        viewHolder.local.setText(subscribe.getLocal());
        viewHolder.date.setText(subscribe.getLocaltime());
        return view;
    }
    class ViewHolder{
        ImageView imageView;
        CircleImageView UserIcon;
        TextView leixing;
        TextView title;
        TextView content;
        TextView date;
        TextView min,max;
        TextView local;
    }
}
