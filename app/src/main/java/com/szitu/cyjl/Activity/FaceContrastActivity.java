package com.szitu.cyjl.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.szitu.cyjl.R;
import com.szitu.cyjl.camera.CameraPreviewListener;
import com.szitu.cyjl.camera.FaceCamera;
import com.szitu.cyjl.engine.FaceRecognitionService;
import com.szitu.cyjl.engine.FaceSerchListener;

import java.util.ArrayList;
import java.util.List;
// 人脸对比
public class FaceContrastActivity extends Activity implements CameraPreviewListener {

    // 人脸对比
    private FaceRecognitionService faceRecognitionService;

    private TextView find_face;
    private ImageView image_view_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_face_contrast);

        find_face = findViewById(R.id.find_face);
        SurfaceView surface_contrast = findViewById(R.id.surface_contrast);
        SurfaceView surface_rect = findViewById(R.id.surface_rect);
        image_view_icon = findViewById(R.id.image_view_icon);


        FaceCamera.getInstance().setCameraPreviewListener(this);
        FaceCamera.getInstance().openCamera(this, surface_contrast, surface_rect);
    }
    @Override
    public void onPreviewData(byte[] data, List<AFT_FSDKFace> fsdkFaces) {
        detect(data, fsdkFaces);
    }

    @Override
    public void onPreviewSize(int width, int height) {
        faceRecognitionService = new FaceRecognitionService(width, height);
    }

    @Override
    public void onPreviewNoFace() {

    }

    @Override
    public void onFaceRect(Rect adjustedRect) {

    }
    // 是否画出人脸的矩形，返回true表示画出人脸的矩形
    @Override
    public boolean isDrawFaceRect() {
        return true;
    }

    private int flag = 1;

    // 开始检测
    public synchronized void detect(final byte[] data, final List<AFT_FSDKFace> fsdkFaces) {
        // 如果有人脸进行注册、识别
        if (fsdkFaces.size() > 0) {
            final AFT_FSDKFace aft_fsdkFace = fsdkFaces.get(0).clone();
            // 人脸对比
            AFR_FSDKFace afr_fsdkFace = faceRecognitionService.faceData(data, aft_fsdkFace.getRect(), aft_fsdkFace.getDegree());
            List<byte[]> faceList = new ArrayList<>();
//            for (Face face : mFaces) {
//                faceList.add(face.getData());
//            }
            faceList.add(My.mFace);

            faceRecognitionService.faceSerch(afr_fsdkFace.getFeatureData(), faceList, new FaceSerchListener() {
                @Override
                public void serchFinish(float sorce, int position) {
                    if (sorce > 0.7) {
                        if (flag == 1) {
                            flag = -1;
                            //find_face.setText("识别成功，正在跳转，请稍后...");
                            find_face.setText("相似度"+(sorce*100)+"%");
                            Bitmap bitmap = BitmapFactory.decodeByteArray(My.mIcon, 0, My.mIcon.length);
                            Bitmap map = rotateBitmap(bitmap,-90);
                            image_view_icon.setImageBitmap(map);

                            // 跳转到密码修改
                            Intent intent = new Intent(FaceContrastActivity.this, AlertPassword.class);
                            startActivity(intent);

                            // 销毁当前页面
                            finish();
                        }
                    } else if (sorce == 0) {
                        //find_face.setText("未注册用户，请先注册!");
                        find_face.setText("相似度"+(sorce*100)+"%");
                    } else {
                        //find_face.setText("请将脸部对准中央，请保持光照充足！");
                    }
                }
            });
        } else {
            find_face.setText("没有检测到人脸！");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        faceRecognitionService.destroyEngine();
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
