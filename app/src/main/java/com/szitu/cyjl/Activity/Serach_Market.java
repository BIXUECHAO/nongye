package com.szitu.cyjl.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.szitu.cyjl.JavaBean.recycle_bean;
import com.szitu.cyjl.Json.Photo_json;
import com.szitu.cyjl.R;
import com.szitu.cyjl.util.FileChooseUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import static com.szitu.cyjl.Activity.PhotoQuery.TAKE_PHOTO;

public class Serach_Market extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Serach_Market";

    private Toolbar toolbar;
    private EditText tb_et_serch;
    private ImageView tb_iv_serch,tb_iv_black,iv_search;

    private PopupWindow popupWindow;
    private View contentView;
    private Button ok,delect,retu;
    private String name;

    private Uri imageUri;
    private File outputImage;
    private Bitmap bitmap;

    public static final String APP_ID = "14561570";
    public static final String API_KEY = "EI3LygyPI3CD45ecaEpqazk9";
    public static final String SECRET_KEY = "xl4Y3Z7THiuGT7ZmtePI3sEArn0QzR0B";

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach__market);
        initView();
        showPopwindow();
    }
    private void initView(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tb_et_serch = (EditText)findViewById(R.id.tb_et_search);
        tb_iv_serch = (ImageView)findViewById(R.id.tb_iv_search);
        tb_iv_serch.setOnClickListener(this);
        tb_iv_black = (ImageView)findViewById(R.id.tb_iv_black);
        tb_iv_black.setOnClickListener(this);
        iv_search = (ImageView)findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_iv_black:
                finish();
                break;
            case R.id.tb_iv_search:
                name = tb_et_serch.getText().toString();
                if(name == null || name.equals("")){
                    toast("请输入农作物");
                    return;
                }
                startIntent(name,null);
                break;
            case R.id.iv_search:
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.button_ok:
                photograph();
                if(popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            case R.id.button_delete:
                photo();
                if(popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
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
        contentView = LayoutInflater.from(Serach_Market.this).inflate(
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

    }
    private void photograph(){
        outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(Serach_Market.this, "com.example.cameraalbumtest.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
    private void photo(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");//选择图片
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择上传的文件"), 67);
        }catch (ActivityNotFoundException ex){
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }
    private void toast(String content){
        Toast.makeText(Serach_Market.this,content,Toast.LENGTH_LONG).show();
    }
    private void startIntent(String name,String path){
        Intent intent = new Intent(Serach_Market.this,Show_Market_Data.class);
        intent.putExtra("name",name);
        intent.putExtra("path",path);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        final String path = outputImage.getAbsolutePath();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                find(path);
                            }
                        }).start();
                        dialog();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 67:
                try {
                    //该uri是上一个Activity返回的
                    imageUri = data.getData();

                    if(imageUri!=null) {
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        final String path = FileChooseUtil.getInstance(this).getChooseFileResultPath(data.getData());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                find(path);
                            }
                        }).start();
                        dialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    private void find(final String path){
        Log.d(TAG, "find: "+path);
        AipImageClassify client=new AipImageClassify(APP_ID,API_KEY,SECRET_KEY);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        HashMap<String, String> options = new HashMap<String, String>();
        options.put("baike_num", "5");
        // 参数为二进制数组
        final JSONObject res = client.advancedGeneral(path, options);
        Log.d(TAG, "find: "+res);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String data[]=Photo_json.getTitle(res);
                name = data[0];
                startIntent(name,path);
                progressDialog.dismiss();
            }
        });
    }
    private void dialog(){
        progressDialog=new ProgressDialog(Serach_Market.this);
        progressDialog.setMessage("识别中...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
