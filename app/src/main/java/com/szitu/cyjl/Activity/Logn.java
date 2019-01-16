package com.szitu.cyjl.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.szitu.cyjl.BasedActivity.BasedActivity;
import com.szitu.cyjl.JavaBean.Activity_Manage;
import com.szitu.cyjl.R;

import java.util.jar.Manifest;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class Logn extends BasedActivity {
    private static final String TAG = "三";
    private EditText et_username,et_password;
    private Button bt_logn;
    private TextView tv_register;
    private TextView forget_password;
    private long fristTime=0;
    private BmobUser bu2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        BmobUser currentUser = BmobUser.getCurrentUser();
        if (currentUser != null) {     // 在加载布局文件前判断是否登陆过
            Intent intent = new Intent(Logn.this,MainActivity.class);
            startActivity(intent);
        }
        Bmob.initialize(this, "900b2dcf667a67b3403f4953c227c0cb");
        setContentView(R.layout.activity_logn);
        initView();
        initLitener();
    }
    private void initView(){
        //用户名
        et_username=(EditText)findViewById(R.id.et_username);
        //密码
        et_password=(EditText)findViewById(R.id.et_password);
        //登录
        bt_logn=(Button)findViewById(R.id.bt_logn);
        //注册
        tv_register=(TextView)findViewById(R.id.tv_register);

        forget_password=(TextView)findViewById(R.id.forget_password);
    }
    private void initLitener(){
        bt_logn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence username=et_username.getText();
                CharSequence password=et_password.getText();
                String Username = et_username.getText().toString();
                if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"用户名.密码不能为空",Toast.LENGTH_SHORT).show();
                }else if (Username.length() != 11 && Username.startsWith("1")){
                    Toast.makeText(getApplicationContext(),"手机格式不正确",Toast.LENGTH_SHORT).show();
                }
                    else {
                    bu2 = new BmobUser();
                    bu2.setUsername(et_username.getText().toString());
                    bu2.setPassword(et_password.getText().toString());
                    bu2.login(new SaveListener<BmobUser>() {

                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if(e==null){
                                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Logn.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(Logn.this,Register.class);
                startActivity(intent);
            }
        });
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Logn.this,Forget_password.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        long lastTime=System.currentTimeMillis();
        if (lastTime - fristTime > 2000){
            Toast.makeText(getApplicationContext(),"再按一次退出应用",Toast.LENGTH_SHORT).show();
            fristTime=lastTime;
        }else {
            Activity_Manage.clerActivity();
        }
    }
}
