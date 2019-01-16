package com.szitu.cyjl.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.szitu.cyjl.BasedActivity.BasedActivity;
import com.szitu.cyjl.Json.Photo_json;
import com.szitu.cyjl.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhotoQuery extends BasedActivity {
    private static final String TAG = "三";
    private Toolbar toolbar;
    private TextView tb_text;
    private TextView tb_local;
    public static final int TAKE_PHOTO = 1;
    private CircleImageView photo;
    private TextView title,root,content;
    private Uri imageUri;
    private File outputImage;
    public static final String APP_ID = "14561570";
    public static final String API_KEY = "EI3LygyPI3CD45ecaEpqazk9";
    public static final String SECRET_KEY = "xl4Y3Z7THiuGT7ZmtePI3sEArn0QzR0B";
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_query);
        initView();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tb_text.setText("图像查询");
        photograph();
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.fanhui);
        }
    }
    private void initView(){
        photo=(CircleImageView)findViewById(R.id.p_iv_photo);
        title=(TextView)findViewById(R.id.tv_title);
        content=(TextView)findViewById(R.id.tv_content);
        root=(TextView)findViewById(R.id.root);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        tb_text=(TextView)findViewById(R.id.tb_text);
        tb_local=(TextView)findViewById(R.id.tb_local);
        tb_local.setVisibility(View.GONE);
    }
    private void photograph(){
        //创建File对象,用于存储拍照后的图片
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
            imageUri = FileProvider.getUriForFile(PhotoQuery.this, "com.example.cameraalbumtest.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                find(outputImage.getAbsolutePath());
                            }
                        }).start();
                        photo.setImageBitmap(bitmap);
                        dialog();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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
    private void dialog(){
        progressDialog=new ProgressDialog(PhotoQuery.this);
        progressDialog.setMessage("识别中...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

}
