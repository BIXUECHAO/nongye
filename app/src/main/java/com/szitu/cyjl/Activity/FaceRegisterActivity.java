package com.szitu.cyjl.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.User;
import com.szitu.cyjl.camera.CameraPreviewListener;
import com.szitu.cyjl.camera.FaceCamera;
import com.szitu.cyjl.data.Face;
import com.szitu.cyjl.data.Person;
import com.szitu.cyjl.engine.FaceRecognitionService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

// 人脸注册
public class FaceRegisterActivity extends AppCompatActivity implements CameraPreviewListener {

    private FaceRecognitionService faceRecognitionService;
    public static List<Face> registerFaces = new ArrayList<>();

//    private String mIMEI;
 //   private String name;
    private ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_face_register);
        Bmob.initialize(this, "900b2dcf667a67b3403f4953c227c0cb");
//        mIMEI = getIntent().getStringExtra("IMEI");
 //       name = getIntent().getStringExtra("name");

        SurfaceView surface_register = findViewById(R.id.surface_register);
        SurfaceView surface_rect = findViewById(R.id.surface_rect);
        icon = findViewById(R.id.icon);

        FaceCamera.getInstance().setCameraPreviewListener(this);
        FaceCamera.getInstance().openCamera(this, surface_register, surface_rect);
    }


    @Override
    public void onPreviewData(byte[] data, List<AFT_FSDKFace> fsdkFaces) {
        detect(data, fsdkFaces);
    }

    private int mWidth, mHeight;

    @Override
    public void onPreviewSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        Log.i("msg", "width-->" + width + "height-->" + height);
        faceRecognitionService = new FaceRecognitionService(width, height);
    }

    // 是否画出人脸的矩形，返回true表示画出人脸的矩形
    @Override
    public boolean isDrawFaceRect() {
        return true;
    }

    @Override
    public void onPreviewNoFace() {

    }

    @Override
    public void onFaceRect(Rect adjustedRect) {

    }


    private int flag = 1;

    // 开始检测
    // data 帧数据
    public synchronized void detect(final byte[] data, final List<AFT_FSDKFace> fsdkFaces) {
        if (fsdkFaces.size() > 0) {
            final AFT_FSDKFace aft_fsdkFace = fsdkFaces.get(0).clone();

            if (flag == 1) {
                flag = -1;

                YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, mWidth, mHeight, null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                yuvimage.compressToJpeg(aft_fsdkFace.getRect(), 80, baos);
                byte[] jdata = baos.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
                icon.setImageBitmap(rotateBitmap(bitmap,-90));

                AFR_FSDKFace afr_fsdkFace = faceRecognitionService.faceData(data, aft_fsdkFace.getRect(), aft_fsdkFace.getDegree());
                finishRegister(afr_fsdkFace.getFeatureData(), jdata);
            }
        }
    }

    // 完成注册
    private void finishRegister(byte[] featureData, byte[] iconData) {
        // 人脸特征
        String feature = Base64.encodeToString(featureData, Base64.NO_WRAP);
        // 脸部图片的Base64编码数据
        String faceImage = Base64.encodeToString(iconData, Base64.NO_WRAP);

        // 创建用户对象
        Person person = new Person();
        person.setUsername(User.getUserInfo().getUsername());         // 用户名
        person.setFaceImage(faceImage);     // 脸部图片
        person.setFeature(feature);         // 人脸的特征值
        person.setIMEI(User.getCurrentUser().getObjectId());
//        person.setIMEI(mIMEI);               // 手机的唯一标识符

        // 添加到Bmob云数据库
        person.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Log.i("msg", "添加数据成功，返回objectId为：" + objectId);
                    showToast("注册成功！");
                } else {
                    Log.i("msg", "创建数据失败：" + e.getMessage());
                    showToast("注册失败！");
                }
            }
        });

        // 销毁当前Activity
        Intent intent = new Intent(FaceRegisterActivity.this,Logn.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        faceRecognitionService.destroyEngine();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

}
