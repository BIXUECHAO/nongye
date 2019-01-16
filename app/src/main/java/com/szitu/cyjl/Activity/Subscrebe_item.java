package com.szitu.cyjl.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.Subscribe;

import java.util.List;

public class Subscrebe_item extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Subscrebe_item";
    private Subscribe subscribe;
    private LinearLayout linearLayout;
    private TextView title,content,local,date,username,min,max,phone,message,spname,spnumber,danwei1,danwei2,danwei3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscrebe_item);
        initView();
        getData();
    }
    private void initView(){
        subscribe = (Subscribe) getIntent().getSerializableExtra("data");
        linearLayout = (LinearLayout)findViewById(R.id.Image_layout);
        content = (TextView)findViewById(R.id.subscribe_content);
        title = (TextView)findViewById(R.id.subscribe_title);
        local = (TextView)findViewById(R.id.subscribe_local);
        date = (TextView)findViewById(R.id.subscribe_date);
        username = (TextView)findViewById(R.id.subscribe_username);
        min = (TextView)findViewById(R.id.subscribe_min);
        max = (TextView)findViewById(R.id.subscribe_max);
        phone = (TextView)findViewById(R.id.subscribe_phone);
        phone.setOnClickListener(this);
        message = (TextView)findViewById(R.id.subscribe_message);
        message.setOnClickListener(this);
        spname = (TextView)findViewById(R.id.subscribe_spname);
        spnumber = (TextView)findViewById(R.id.spnumber);
        danwei1 = (TextView)findViewById(R.id.danwei1);
        danwei2 = (TextView)findViewById(R.id.danwei2);
        danwei3 = (TextView)findViewById(R.id.danwei3);
        Log.d(TAG, "initView: 电话  "+subscribe.getPhone());
    }
    private void getData(){
        if(subscribe.getType().equals("农机")){
            spnumber.setVisibility(View.GONE);
            danwei2.setVisibility(View.GONE);
            danwei3.setVisibility(View.GONE);
            danwei1.setText("元/亩");
        }else if(subscribe.getType().equals("人员互助")){
            spnumber.setText(subscribe.getSp_number());
            danwei1.setText("元/亩");
            danwei2.setText("人");
            danwei3.setText("总人数");
        }else if(subscribe.getType().equals("农作物")){
            spnumber.setText(subscribe.getSp_number());
            danwei1.setText("元/千克");
            danwei2.setText("千克");
            danwei3.setText("库存");
        }else {

        }
        spname.setText(subscribe.getSp_name());
        List<String> list = subscribe.getFilebyte();
        for(String s : list){
            ImageView imageView = new ImageView(Subscrebe_item.this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));  //设置图片宽高
            Glide.with(Subscrebe_item.this).load(s).placeholder(R.drawable.logo).into(imageView);
            linearLayout.addView(imageView);
        }
        title.setText(subscribe.getType());
        local.setText(subscribe.getLocal());
        date.setText(subscribe.getLocaltime());
        username.setText(subscribe.getUsername());
        min.setText(subscribe.getMin());
        max.setText(subscribe.getMax());
        content.setText(subscribe.getIntroduce());
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.subscribe_phone:
                if(ContextCompat.checkSelfPermission(Subscrebe_item.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Subscrebe_item.this,new String[]{Manifest.permission.CALL_PHONE},1);
                }else {
                    call();
                }
                break;
            case R.id.subscribe_message:
                Intent intent=new Intent(Subscrebe_item.this,SendMessage.class);
                intent.putExtra("userObjectID",subscribe.getUserOblectID());
                intent.putExtra("username",subscribe.getUsername());
                intent.putExtra("userPhone",subscribe.getPhone());
                startActivity(intent);
                break;
                default:break;
        }
    }
    private void call(){
        try{
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+subscribe.getPhone()));
            startActivity(intent);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int a : grantResults){
                        if(a != PackageManager.PERMISSION_GRANTED){
                            toast("权限未开启");
                        }
                    }
                }
        }
    }
    private void toast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT).show();
    }
}
