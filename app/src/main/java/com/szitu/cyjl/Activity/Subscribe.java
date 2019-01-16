package com.szitu.cyjl.Activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.szitu.cyjl.BasedActivity.BasedActivity;
import com.szitu.cyjl.R;

public class Subscribe extends BasedActivity implements View.OnClickListener {
    private TextView textView,ok,delect,retu;
    private PopupWindow popupWindow;
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subscribe);
        initView();
        showPopwindow();
    }
    private void initView(){
        textView = (TextView)findViewById(R.id.aaa);
        textView.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aaa:
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.button_ok:
                Toast("拍照");
                break;
            case R.id.button_delete:
                Toast("相册");
                break;
            case R.id.button_return:
                if(popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
                default:break;
        }
    }
    private void showPopwindow() {
        //加载弹出框的布局
        contentView = LayoutInflater.from(Subscribe.this).inflate(
                R.layout.pop, null);
        ok = (Button)contentView.findViewById(R.id.button_ok);
        ok.setOnClickListener(this);
        delect = (Button)contentView.findViewById(R.id.button_delete);
        delect.setOnClickListener(this);
        retu = (Button)contentView.findViewById(R.id.button_return);
        retu.setOnClickListener(this);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画，指定刚才定义的style
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        // 按下android回退物理键 PopipWindow消失解决

    }
    private void Toast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if(popupWindow!=null&&popupWindow.isShowing()){
            popupWindow.dismiss();
        }
        super.onBackPressed();
    }
}
