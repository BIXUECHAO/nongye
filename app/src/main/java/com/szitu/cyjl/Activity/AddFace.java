package com.szitu.cyjl.Activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.szitu.cyjl.BasedActivity.BasedActivity;
import com.szitu.cyjl.R;

public class AddFace extends BasedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_face);
    }
}
