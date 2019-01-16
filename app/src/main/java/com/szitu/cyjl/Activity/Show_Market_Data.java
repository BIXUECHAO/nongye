package com.szitu.cyjl.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.szitu.cyjl.Adapter.Price_Adapter;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.Market;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Show_Market_Data extends AppCompatActivity implements View.OnClickListener {
    private String sp_name;
    private String sp_path;
    private ImageView name_icon;
    private TextView name;
    private ImageView iv_black;
    private ListView listView;
    private Price_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show__market__data);
        initView();
    }
    private void initView(){
        sp_name = getIntent().getStringExtra("name");
        sp_path = getIntent().getStringExtra("path");
        name_icon = (ImageView)findViewById(R.id.name_icon);
        Glide.with(this).load(sp_path).placeholder(R.drawable.market_data_bg).into(name_icon);
        iv_black = (ImageView)findViewById(R.id.iv_black);
        iv_black.setOnClickListener(this);
        name = (TextView)findViewById(R.id.name);
        name.setText(sp_name);
        listView = (ListView)findViewById(R.id.market_listView);
        initData(sp_name);
    }
    private void initData(String name){
        if(name.contains("蒜")){
            selectData("大蒜");
        }else if(name.contains("豆")){
            selectData("大豆");
        }else if(name.contains("玉米")){
            selectData("玉米");
        }
        else {
            selectData(name);
        }

    }
    private void selectData(String name){
        BmobQuery<Market> query = new BmobQuery<>();
        query.addWhereEqualTo("name",name);
        query.findObjects(new FindListener<Market>() {
            @Override
            public void done(List<Market> list, BmobException e) {
                if(e==null){
                    showData(list);
                }else {
                    toast("网络出错");
                }
            }
        });
    }
    private void showData(List<Market> list){
        adapter = new Price_Adapter(Show_Market_Data.this,R.layout.show_price_item,list);
        listView.setAdapter(adapter);
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_black:
                finish();
                break;
                default:break;
        }
    }
    private void toast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT).show();
    }
}
