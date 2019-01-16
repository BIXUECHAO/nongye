package com.szitu.cyjl.camera;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.arcsoft.facetracking.AFT_FSDKFace;
import com.szitu.cyjl.engine.FaceTrackEngine;
import com.szitu.cyjl.util.DrawUtils;
import com.szitu.cyjl.util.ImageUtils;

import java.util.List;

public class FaceCamera implements SurfaceHolder.Callback {

    private Camera camera;

    private Activity activity;
    private SurfaceView surface_preview, surface_rect;
    private CameraPreviewListener cameraPreviewListener;


    private int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int cameraOri = 90; // 相机的方向

    private FaceTrackEngine faceTrackEngine; // 人脸追踪引擎

    public void openCamera(Activity activity, SurfaceView surfacePreview, SurfaceView surfaceViewRect) {
        this.activity = activity;
        this.surface_preview = surfacePreview;
        this.surface_rect = surfaceViewRect;

        surface_preview.getHolder().addCallback(this);
        surface_rect.setZOrderMediaOverlay(true);
        surface_rect.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 打开前置摄像头
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        try {
            // 设置摄像头的参数
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size previewSize = getBestSupportedSize(parameters.getSupportedPreviewSizes(), metrics);
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            parameters.setPreviewFormat(ImageFormat.NV21);
            camera.setParameters(parameters);

            // 回调接口
            if (cameraPreviewListener != null) {
                cameraPreviewListener.onPreviewSize(previewSize.width, previewSize.height);
            }
            // 设置相机方向
            setCameraDisplayOrientation(activity, cameraId, camera);
            camera.setPreviewDisplay(holder);
            faceTrackEngine = new FaceTrackEngine(previewSize.width, previewSize.height);
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    //获取人脸的位置信息
                    List<AFT_FSDKFace> fsdkFaces = faceTrackEngine.getFaces(data);
                    //画出人脸的位置
                    drawFaceRect(fsdkFaces);
                    //输出数据进行其他处理
                    if ((cameraPreviewListener != null && fsdkFaces.size() > 0)) {
                        cameraPreviewListener.onPreviewData(data.clone(), fsdkFaces);
                    } else if ((cameraPreviewListener != null)) {
                        cameraPreviewListener.onPreviewNoFace();
                    }
                }
            });
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void drawFaceRect(List<AFT_FSDKFace> fsdkFaces) {
        // 获取最大的人脸
        int maxIndex = ImageUtils.findFTMaxAreaFace(fsdkFaces);
        if (surface_rect != null) {
            Canvas canvas = surface_rect.getHolder().lockCanvas();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(5);
            paint.setTextSize(80);

            if (fsdkFaces.size() > 0) {
                for (AFT_FSDKFace aft_fsdkFace : fsdkFaces) {
                    Rect rect = new Rect(aft_fsdkFace.getRect());

                    if (rect != null) {
                        Rect adjustedRect = DrawUtils.adjustRect(rect, faceTrackEngine.getWidth(), faceTrackEngine.getHeight(),
                                canvas.getWidth(), canvas.getHeight(), cameraOri, cameraId);
                        // 如果是要画出人脸矩形
                        if (cameraPreviewListener.isDrawFaceRect()) {
                            // 画出人脸矩形
                            DrawUtils.drawFaceRect(canvas, adjustedRect, Color.YELLOW, 4);
                        }
                        // 回调
                        cameraPreviewListener.onFaceRect(adjustedRect);
                    }

                }
            }
            surface_rect.getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        closeCamera();
    }

    // 关闭相机
    private void closeCamera() {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
        faceTrackEngine.destoryEngine();
    }

    // 设置相机预览分辨率
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, DisplayMetrics metrics) {
        Camera.Size bestSize = sizes.get(0);
        float screenRatio = (float) metrics.widthPixels / (float) metrics.heightPixels;
        if (screenRatio > 1) {
            screenRatio = 1 / screenRatio;
        }

        for (Camera.Size s : sizes) {
            if (Math.abs((s.height / (float) s.width) - screenRatio) < Math.abs(bestSize.height /
                    (float) bestSize.width - screenRatio)) {
                bestSize = s;
            }
        }
        return bestSize;
    }

    // 设置相机方向
    private void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        cameraOri = result;
        camera.setDisplayOrientation(result);
    }

    // 设置监听
    public void setCameraPreviewListener(CameraPreviewListener cameraPreviewListener) {
        this.cameraPreviewListener = cameraPreviewListener;
    }

    private FaceCamera() {

    }

    public static FaceCamera getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        public static FaceCamera INSTANCE = new FaceCamera();
    }


}
