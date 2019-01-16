package com.szitu.cyjl.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.szitu.cyjl.JavaBean.recycle_bean;
import com.szitu.cyjl.R;

import java.util.ArrayList;
import java.util.List;

public class Recycle_Adapter extends RecyclerView.Adapter<Recycle_Adapter.ViewHolder> {
    private ArrayList<recycle_bean> list;
    private Context context;
    public Recycle_Adapter(ArrayList<recycle_bean> lists){
        list = lists;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = View.inflate(parent.getContext(),R.layout.recycle_view,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        recycle_bean bean = list.get(position);
        String path = bean.getPath();
        Glide.with(context).load(path).placeholder(R.drawable.logo).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recy_Image);
        }
    }
}
