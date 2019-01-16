package com.szitu.cyjl.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szitu.cyjl.BasedActivity.BasedActivity;
import com.szitu.cyjl.JavaBean.Activity_Manage;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.User;
import com.szitu.cyjl.data.Person;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class My extends BasedActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView tb_text;
    private ImageView usericon;
    private static final String TAG = "My";
    private RelativeLayout alert_password;
    private TextView logout,usename,youxiang,phone;
    public static byte[] mFace; // 人脸特征值
    public static byte[] mIcon; // 用户的脸部图片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        Bmob.initialize(this, "900b2dcf667a67b3403f4953c227c0cb");
        setContentView(R.layout.activity_my);
        initView();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tb_text.setText("个人中心");
        initListener();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.fanhui);
        }
        usename.setText(User.getUserInfo().getUsername());
        youxiang.setText(User.getUserInfo().getEmail());
        phone.setText(User.getUserInfo().getMobilePhoneNumber());
    }
    private void initView(){
        alert_password=(RelativeLayout)findViewById(R.id.changePassword);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tb_text = (TextView) findViewById(R.id.tb_text);
        logout = (TextView) findViewById(R.id.logout);
        usename = (TextView) findViewById(R.id.usernameText);
        youxiang = (TextView) findViewById(R.id.emailText);
        phone = (TextView)findViewById(R.id.phoneText);
        usericon = (ImageView) findViewById(R.id.usericon);
        usericon.setOnClickListener(this);
    }
    private void initListener(){
        alert_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToFace();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert();
            }
        });
    }
    private void ToFace(){
        query();
    }
    private void query() {
        BmobQuery<Person> query = new BmobQuery<>();
        query.addWhereEqualTo("IMEI", User.getUserInfo().getObjectId());
        Log.d(TAG, "query: "+User.getUserInfo().getObjectId());
        query.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> object, BmobException e) {
                if (e == null) {
                    Log.i("msg", "查询成功：共" + object.size() + "条数据。");
                    for (Person person : object) {
                        Log.i("msg", "IMEI-->" + person.getIMEI());
                        Log.i("msg", "objectId-->" + person.getObjectId());
                    }
                    if (object.size() == 0) {
                        // 说明该用户没有注册过，无法进行人脸验证
                        Toast.makeText(getApplicationContext(),"您未注册，请先登录",Toast.LENGTH_SHORT).show();
                    } else {
                        // 该用户已经注册过，开始人脸验证
                        mFace = Base64.decode(object.get(0).getFeature(), Base64.NO_WRAP);
                        Log.d(TAG, "done: "+mFace);
                        mIcon = Base64.decode(object.get(0).getFaceImage(), Base64.NO_WRAP);
                        Log.d(TAG, "done: "+mIcon);
                        Toast.makeText(getApplicationContext(),"进入人脸验证",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(My.this, FaceContrastActivity.class));
                    }
                } else {
                    Log.i("msg", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    // 如果出错
                    Toast.makeText(getApplicationContext(),"网络出错",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
                default:break;
        }
        return true;
    }
    private void alert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(My.this)
                .setTitle("切换用户？")
                .setMessage("切换用户会注销本次账号")
                .setCancelable(false)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BmobUser.logOut();
                        Activity_Manage.clerActivity();
                        Intent intent3=new Intent(My.this,Logn.class);
                        startActivity(intent3);
                    }
                });
        builder.show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.usericon:

                break;
                default:break;
        }
    }

}
