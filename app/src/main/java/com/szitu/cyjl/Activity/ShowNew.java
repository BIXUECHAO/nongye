package com.szitu.cyjl.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.MyNew;

public class ShowNew extends AppCompatActivity {
    private TextView n_naem,n_come,n_content,n_date;
    private ImageView n_icon;
    private MyNew myNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_new);
        initView();
    }
    private void initView(){
        myNew = (MyNew) getIntent().getSerializableExtra("data");
        n_naem = (TextView)findViewById(R.id.new_name);
        n_naem.setText(myNew.getName());
        n_come = (TextView)findViewById(R.id.new_come);
        n_come.append(myNew.getCome());
        n_content = (TextView)findViewById(R.id.new_content);
        n_content.setText(myNew.getContent());
        n_date = (TextView)findViewById(R.id.new_date);
        n_date.setText(myNew.getDate());
        n_icon = (ImageView)findViewById(R.id.new_icon);
        Glide.with(this).load(myNew.getUrl()).placeholder(R.drawable.logo).into(n_icon);
    }
}
