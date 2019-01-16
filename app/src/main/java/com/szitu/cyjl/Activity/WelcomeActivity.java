package com.szitu.cyjl.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.szitu.cyjl.Json.WelcomeJson;
import com.szitu.cyjl.R;
import com.szitu.cyjl.Welcome.ParticleView;

import org.apache.log4j.chainsaw.Main;
import org.json.JSONArray;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "三";
    private TextView time, tiaoguo;
    private LinearLayout linearLayout;
    private ImageView imageView;
    private ParticleView mPv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        Bmob.initialize(this, "900b2dcf667a67b3403f4953c227c0cb");
        setContentView(R.layout.activity_welcome);
        initView();
        initListener();
        TimeSleep();
        queryData();

        mPv1.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                intent();
            }
        });
        mPv1.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPv1.startAnim();
            }
        }, 200);

    }
    private void TimeSleep() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 2; i >= 1; i--) {
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            time.setText(finalI + "");
                        }
                    });
                    SystemClock.sleep(1000);
                }
            }
        }).start();
    }
    private void initView() {
        time = (TextView) findViewById(R.id.time);
        tiaoguo = (TextView) findViewById(R.id.tiaoguo);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        imageView = (ImageView) findViewById(R.id.We_imageView);
        mPv1 = (ParticleView) findViewById(R.id.pv_1);
    }
    private void initListener() {
        tiaoguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent();
            }
        });
    }
    private void intent() {
        Intent intent = new Intent(WelcomeActivity.this,Logn.class);
        startActivity(intent);
        finish();
    }
    public void queryData() {
        BmobQuery query = new BmobQuery("Welcome");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    Log.d(TAG, "done: " + ary.toString());
                    String url=WelcomeJson.getJson(ary);
                    Log.d(TAG, "done: "+url);
                    Glide.with(WelcomeActivity.this).load(url).into(imageView);
                } else {
                    Log.i(TAG, "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
