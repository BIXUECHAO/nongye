package com.szitu.cyjl.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.szitu.cyjl.Activity.SendMessage;
import com.szitu.cyjl.Adapter.Message_Adapter;
import com.szitu.cyjl.JavaBean.Message;
import com.szitu.cyjl.R;

import java.util.ArrayList;

public class Last_Xiaoxi_fragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayList<Message> messages;
    private Message_Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lat_xiaoxi,container,false);
        initView(view);
        return view;
    }
    private void initView(View view){
        listView = (ListView)view.findViewById(R.id.message_list);
        listView.setOnItemClickListener(this);
        initList();
        if(messages.size() == 0){

        }
        adapter = new Message_Adapter(getActivity(),R.layout.message_item,messages);
        listView.setAdapter(adapter);
    }
    private void initList(){
        messages = new ArrayList<>();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(),SendMessage.class));
    }
}
