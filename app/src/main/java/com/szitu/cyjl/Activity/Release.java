package com.szitu.cyjl.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.szitu.cyjl.BasedActivity.BasedActivity;
import com.szitu.cyjl.R;

import cn.bmob.v3.Bmob;

public class Release extends BasedActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView subscribe,bazaar,knowledge,forum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        Bmob.initialize(this, "900b2dcf667a67b3403f4953c227c0cb");
        setContentView(R.layout.activity_release);
        initView();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.fanhui);
        }
    }
    private void initView(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        subscribe = (TextView)findViewById(R.id.release_subscribe);
        subscribe.setOnClickListener(this);
        bazaar = (TextView)findViewById(R.id.release_bazaar);
        bazaar.setOnClickListener(this);
        knowledge = (TextView)findViewById(R.id.release_knowledge);
        knowledge.setOnClickListener(this);
        forum = (TextView)findViewById(R.id.release_forum);
        forum.setOnClickListener(this);
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.release_subscribe:
                startActivity(new Intent(Release.this,Rrelease_Subscribe.class));
                break;
            case R.id.release_bazaar:
                startActivity(new Intent(Release.this,Release_shangpin.class));
                break;
            case R.id.release_knowledge:

                break;
            case R.id.release_forum:

                break;
                default:break;
        }
    }
}
