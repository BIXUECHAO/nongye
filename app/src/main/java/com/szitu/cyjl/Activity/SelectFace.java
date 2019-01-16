package com.szitu.cyjl.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.szitu.cyjl.BasedActivity.BasedActivity;
import com.szitu.cyjl.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class SelectFace extends BasedActivity {
    private static final String TAG = "SelectFace";
    private Toolbar toolbar;
    private TextView textView;
    private EditText new_password,two_new_password;
    private String yanzhengma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        Bmob.initialize(this, "900b2dcf667a67b3403f4953c227c0cb");
        setContentView(R.layout.activity_selectface);
        initView();
        yanzhengma = getIntent().getStringExtra("yanzhengma");
        toolbar.setTitle("");
        textView.setText("更换密码");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.fanhui);
        }
    }
    private void initView(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        textView=(TextView)findViewById(R.id.tb_text);
        new_password=(EditText)findViewById(R.id.ed_new_password);
        two_new_password=(EditText)findViewById(R.id.ed_two_new_password);
    }
    public void WonClick(View view){
        String p1=new_password.getText().toString();
        String p2=two_new_password.getText().toString();
        CharSequence t2=new_password.getText();
        CharSequence t3=two_new_password.getText();
        if(TextUtils.isEmpty(t2) || TextUtils.isEmpty(t3)){
            Toast.makeText(getApplicationContext(),"输入不能为空",Toast.LENGTH_SHORT).show();
        }else {
            BmobUser.resetPasswordBySMSCode(yanzhengma,p1, new UpdateListener() {

                @Override
                public void done(BmobException ex) {
                    if(ex==null){
                        Toast.makeText(getApplicationContext(),"密码修改成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SelectFace.this,Logn.class);
                        SystemClock.sleep(1000);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"密码修改失败，请查看验证码是否正确",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "done: "+ex.getMessage());
                    }
                }
            });

        }
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
