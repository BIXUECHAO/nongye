package com.szitu.cyjl.Activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.szitu.cyjl.Json.Photo_json;
import com.szitu.cyjl.R;
import com.szitu.cyjl.util.FileChooseUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import static com.szitu.cyjl.Activity.PhotoQuery.TAKE_PHOTO;

public class Album_Query extends AppCompatActivity {
    private static final String TAG = "Album_Query";
    private Uri imageUri;
    private ImageView photo;
    private ProgressDialog progressDialog;
    private TextView title,root,content;
    private Toolbar toolbar;
    private TextView tb_text,tb_local;
    public static final String APP_ID = "14561570";
    public static final String API_KEY = "EI3LygyPI3CD45ecaEpqazk9";
    public static final String SECRET_KEY = "xl4Y3Z7THiuGT7ZmtePI3sEArn0QzR0B";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_album__query);
        initView();
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.fanhui);
        }
        photo();
    }
    private void initView(){
        photo = (ImageView)findViewById(R.id.p_iv_photo);
        title=(TextView)findViewById(R.id.tv_title);
        content=(TextView)findViewById(R.id.tv_content);
        root=(TextView)findViewById(R.id.root);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tb_text = (TextView)findViewById(R.id.tb_text);
        tb_text.setText("图像查询");
        tb_local = (TextView)findViewById(R.id.tb_local);
        tb_local.setVisibility(View.GONE);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
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
                        photo.setImageBitmap(bit);
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
    private void find(String path){
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
                title.setText(data[0]);
                String a=data[1];
                String b=data[2];
                if(a == null || a.equals("")) {
                    root.setText("");
                }else {
                    root.setText("类型:\n   " + a);
                }
                if(b ==null || b.equals("")) {
                    content.setText("");
                }else {
                    content.setText("简介:\n   " + b);
                }
                progressDialog.dismiss();
            }
        });
    }
    private void dialog(){
        progressDialog=new ProgressDialog(Album_Query.this);
        progressDialog.setMessage("识别中...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
