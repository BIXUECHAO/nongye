package com.szitu.cyjl.engine;

import android.util.Log;

import com.arcsoft.facetracking.AFT_FSDKEngine;
import com.arcsoft.facetracking.AFT_FSDKError;
import com.arcsoft.facetracking.AFT_FSDKFace;

import java.util.ArrayList;
import java.util.List;

// 人脸追踪引擎
public class FaceTrackEngine {

    private AFT_FSDKEngine aft_fsdkEngine; // 人脸追踪引擎
    private String APPID = "C9kbVXERmeoGQafG8R89dUBAKjGHAQcZNN613157qeWp";
    private String FTSDKKEY = "7Y37QExiomhQgNFdbAv9G1HvFrjy3qm7NzLS37JxzEzi";

    private int mWidth, mHeight;
    public FaceTrackEngine(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;

        // 创建人脸追踪引擎
        aft_fsdkEngine = new AFT_FSDKEngine();
        // 初始化人脸追踪引擎
        AFT_FSDKError error = aft_fsdkEngine.AFT_FSDK_InitialFaceEngine(APPID, FTSDKKEY, AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT,
                16, 5);
        Log.i("msg", "初始化人脸追踪引擎-->" + error.getCode());
    }

    // 获取人脸数据
    public List<AFT_FSDKFace> getFaces(byte[] data) {
        // 人脸数据
        List<AFT_FSDKFace> faces = new ArrayList<>();
        // 获取人脸数据
        AFT_FSDKError error = aft_fsdkEngine.AFT_FSDK_FaceFeatureDetect(data, mWidth, mHeight, AFT_FSDKEngine.CP_PAF_NV21, faces);
//        Log.i("msg", "获取人脸数据-->" + error.getCode());

        return faces; // 返回人脸数据
    }

    // 销毁人脸追踪引擎
    public void destoryEngine() {
        AFT_FSDKError error = aft_fsdkEngine.AFT_FSDK_UninitialFaceEngine();
        Log.i("msg", "销毁人脸追踪引擎-->" + error.getCode());
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

}
