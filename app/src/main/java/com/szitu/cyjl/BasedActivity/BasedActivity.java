package com.szitu.cyjl.BasedActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.szitu.cyjl.JavaBean.Activity_Manage;

public class BasedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity_Manage.addActivity(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Activity_Manage.removeActivity(this);
    }
}
