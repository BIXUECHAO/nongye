package com.szitu.cyjl.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.Market;

import java.util.List;

public class Price_Adapter extends ArrayAdapter<Market> {
    int layoutID;
    public Price_Adapter(@NonNull Context context, int resource, @NonNull List<Market> objects) {
        super(context, resource, objects);
        layoutID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Market market = getItem(position);
        View view;
        ViewHolder holder;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_price_item,parent,false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.price_name);
            holder.price = (TextView)view.findViewById(R.id.price_price);
            holder.adrees = (TextView)view.findViewById(R.id.price_adrees);
            holder.twoname = (TextView)view.findViewById(R.id.price_twoname);
            holder.date = (TextView)view.findViewById(R.id.price_date);
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(market.getName());
        holder.price.setText(market.getPrice()+" 元/斤");
        holder.adrees.setText(market.getAdress());
        holder.twoname.setText(market.getTwoname());
        holder.date.setText(market.getDate());
        return view;
    }
    class ViewHolder{
        TextView name;
        TextView price;
        TextView adrees;
        TextView twoname;
        TextView date;
    }
}
