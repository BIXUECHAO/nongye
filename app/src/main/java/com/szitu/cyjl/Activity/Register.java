package com.szitu.cyjl.Activity;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.szitu.cyjl.BasedActivity.BasedActivity;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class Register extends BasedActivity {
    private static final String TAG = "三";
    private Toolbar toolbar;
    private TextView title_text,tb_local;
    private EditText name,password,twopassword,nickname;
    private Button next;
    private User bu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        Bmob.initialize(this, "900b2dcf667a67b3403f4953c227c0cb");
        setContentView(R.layout.activity_register);
        initView();
        toolbar.setTitle("");
        title_text.setText("注册");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.fanhui);
        }
        initLitener();
    }
    private void initView(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        title_text=(TextView)findViewById(R.id.tb_text);
        name=(EditText)findViewById(R.id.ed_username);
        password=(EditText)findViewById(R.id.ed_password);
        twopassword=(EditText)findViewById(R.id.ed_two_password);
        next=(Button)findViewById(R.id.bt_next);
        nickname = (EditText)findViewById(R.id.ed_nickname);
        tb_local = (TextView)findViewById(R.id.tb_local);
        tb_local.setVisibility(View.GONE);
    }
   private void initLitener(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence a1=name.getText();
                CharSequence a2=password.getText();
                CharSequence a3=twopassword.getText();
                CharSequence a4=nickname.getText();
                String b1=name.getText().toString();
                String b2=password.getText().toString();
                String b3=twopassword.getText().toString();
                String b4=nickname.getText().toString();
                if(TextUtils.isEmpty(a1) || TextUtils.isEmpty(a2) || TextUtils.isEmpty(a3)||TextUtils.isEmpty(a4)){
                    Toast.makeText(getApplicationContext(),"输入不能为空",Toast.LENGTH_SHORT).show();
                }else if(! b2.equals(b3)){
                    Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_SHORT).show();
                }else if(b1.length() != 11 || !b1.startsWith("1")){
                    Toast.makeText(getApplicationContext(),"电话号码格式错误",Toast.LENGTH_SHORT).show();
                } else {
                    bu = new User();
                    bu.setUsername(b1);
                    bu.setPassword(b2);
                    bu.setMobilePhoneNumber(b1);
                    bu.setNickname(b4);
                    bu.signUp(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser s, BmobException e) {
                            if(e==null){
                                Toast.makeText(getApplicationContext(),"人脸录入",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Register.this,FaceRegisterActivity.class);
                                SystemClock.sleep(1000);
                                startActivity(intent);
                            }else{
                                String text=e+"";
                                if(text.contains("202")){
                                    Toast.makeText(getApplicationContext(),"此电话号码已绑定用户！",Toast.LENGTH_SHORT).show();
                                }else if(text.contains("209")){
                                    Toast.makeText(getApplicationContext(),"此电话号码已绑定用户！",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"服务器忙请稍后再试",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
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
}
