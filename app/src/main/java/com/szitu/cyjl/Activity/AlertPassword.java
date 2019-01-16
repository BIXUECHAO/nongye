package com.szitu.cyjl.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class AlertPassword extends BasedActivity {
    private static final String TAG = "AlertPassword";
    private Toolbar toolbar;
    private TextView tb_title;
    private EditText usedpassword,newPassword,newtwoPassword;
    private Button bt_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alert_password);
        initView();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.fanhui);
        }
        initListener();
    }
    private void initView(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        tb_title = (TextView)findViewById(R.id.tb_text);
        tb_title.setText("密码修改");
        usedpassword = (EditText)findViewById(R.id.et_used_password);
        newPassword = (EditText)findViewById(R.id.et_new_password);
        newtwoPassword = (EditText)findViewById(R.id.et_two_new_password);
        bt_next = (Button) findViewById(R.id.bt_finsh);
    }
    private void initListener(){
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a1 = usedpassword.getText().toString();
                String a2 = newPassword.getText().toString();
                String a3 = newtwoPassword.getText().toString();
                CharSequence text1 = a1;
                CharSequence text2 = a2;
                CharSequence text3 = a3;
                if(TextUtils.isEmpty(text1) && TextUtils.isEmpty(text2) && TextUtils.isEmpty(text3)){
                    toast("输入不能为空");
                    return;
                }
                if( !newPassword.getText().toString().equals(newtwoPassword.getText().toString())){
                    toast("两次密码不一致");
                    return;
                }
                BmobUser.updateCurrentUserPassword(a1, a2, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                toast("密码修改成功");
                                Intent intent=new Intent(AlertPassword.this,My.class);
                                startActivity(intent);
                                finish();
                            }else{
                                toast("密码修改失败");
                            }
                        }

                    });
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
    private void toast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT).show();
    }
}
