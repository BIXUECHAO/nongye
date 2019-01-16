package com.szitu.cyjl.camera;

import android.graphics.Rect;

import com.arcsoft.facetracking.AFT_FSDKFace;

import java.util.List;

public interface CameraPreviewListener {
    void onPreviewData(byte[] data, List<AFT_FSDKFace> fsdkFaces);
    void onPreviewSize(int width, int height);
    void onPreviewNoFace();
    void onFaceRect(Rect adjustedRect);
    boolean isDrawFaceRect();
}
