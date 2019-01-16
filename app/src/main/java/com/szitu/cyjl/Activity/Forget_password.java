package com.szitu.cyjl.Activity;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import com.szitu.cyjl.data.Person;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class Forget_password extends BasedActivity {
    private static final String TAG = "Forget_password";
    private EditText phone,et_yanzheng;
    private Toolbar toolbar;
    private TextView title_text,tb_local;
    private Button button,bt_yanzheng;
    public static byte[] mFace; // 人脸特征值
    public static byte[] mIcon; // 用户的脸部图片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        Bmob.initialize(this, "900b2dcf667a67b3403f4953c227c0cb");
        setContentView(R.layout.activity_forget_password);
        initView();
        toolbar.setTitle("");
        title_text.setText("忘记密码");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.fanhui);
        }
        initLitener();
    }
    private void initView(){
        phone=(EditText)findViewById(R.id.f_ed_phone);
        et_yanzheng = (EditText)findViewById(R.id.f_ed_yanzheng);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        title_text=(TextView)findViewById(R.id.tb_text);
        button=(Button)findViewById(R.id.f_bt_next);
        bt_yanzheng = (Button)findViewById(R.id.bt_yanzheng);
        tb_local = (TextView)findViewById(R.id.tb_local);
        tb_local.setVisibility(View.GONE);
    }
    private void initLitener(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p=phone.getText().toString();
                String y=et_yanzheng.getText().toString();
                if(p==null||p.equals("")||y==null||y.equals("")){
                    Toast.makeText(getApplicationContext(),"输入不能为空",Toast.LENGTH_SHORT).show();
                } else if(p.length() != 11 || !p.startsWith("1")){
                    Toast.makeText(getApplicationContext(),"电话格式错误",Toast.LENGTH_SHORT).show();
                }else {
                    query(p);
                }

            }
        });
        bt_yanzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p=phone.getText().toString();
                if(p==null||p.equals("")){
                    Toast.makeText(getApplicationContext(),"电话输入不能为空",Toast.LENGTH_SHORT).show();
                } else if(p.length() != 11 || !p.startsWith("1")){
                    Toast.makeText(getApplicationContext(),"电话格式错误",Toast.LENGTH_SHORT).show();
                }else {
                    into();
                }

            }
        });
    }
    private void query(String d) {
        BmobQuery<Person> query = new BmobQuery<>();
        query.addWhereEqualTo("username",d);
        query.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> object, BmobException e) {
                if (e == null) {
                    Log.i("msg", "查询成功：共" + object.size() + "条数据。");
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

                        Intent intent = new Intent(Forget_password.this,Forget_face.class);
                        intent.putExtra("yanzhengma",et_yanzheng.getText().toString());
                        startActivity(intent);
                    }
                } else {
                    Log.i("msg", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    // 如果出错
                    Toast.makeText(getApplicationContext(),"网络出错",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void into(){
        BmobSMS.requestSMSCode(phone.getText().toString(), "farming", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {//验证码发送成功
                    Toast.makeText(getApplicationContext(),"已发送短信，请注意查收",Toast.LENGTH_SHORT).show();
                    bt_yanzheng.setClickable(false);
                    Mytime();
                }else {
                    Toast.makeText(getApplicationContext(),"验证码发送失败",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "done: 失败"+ex.getMessage());
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
    private void Mytime(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=60;i>=0;i--){
                    SystemClock.sleep(1000);
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bt_yanzheng.setText(finalI +"秒后可重试");
                            if(finalI==0){
                                bt_yanzheng.setText("重新获取");
                                bt_yanzheng.setClickable(true);
                            }
                        }
                    });
                }
            }
        }).start();
    }
}
