package com.szitu.cyjl.engine;

import android.graphics.Rect;
import android.util.Log;

import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKMatching;

import java.util.List;

// 人脸对比，人脸识别，获取人脸特征
public class FaceRecognitionService {

    // 密钥
    private String APPID = "C9kbVXERmeoGQafG8R89dUBAKjGHAQcZNN613157qeWp";
    private String FR_SDKKEY = "7Y37QExiomhQgNFdbAv9G1JY4s3mDJ9HeEuv7SP16M7u";
    // 人脸对比引擎
    private AFR_FSDKEngine afr_fsdkEngine;
    private int mWidth, mHeight;

    public FaceRecognitionService(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;

        afr_fsdkEngine = new AFR_FSDKEngine();
        AFR_FSDKError error = afr_fsdkEngine.AFR_FSDK_InitialEngine(APPID, FR_SDKKEY);

        Log.i("msg", "初始化人脸对比引擎-->" + error.getCode());
    }

    // 获取人脸特征
    public AFR_FSDKFace faceData(byte[] data, Rect rect, int degree) {
        AFR_FSDKFace face = new AFR_FSDKFace();
        afr_fsdkEngine.AFR_FSDK_ExtractFRFeature(data, mWidth, mHeight, AFR_FSDKEngine.CP_PAF_NV21,
                rect, degree, face);

        return face;
    }

    // 人脸对比
    public float faceRecognition(byte[] faceData1, byte[] faceData2) {
        //score用于存放人脸对比的相似度值
        AFR_FSDKMatching score = new AFR_FSDKMatching();
        //用来存放提取到的人脸信息, face_1是注册的人脸，face_2是要识别的人脸
        AFR_FSDKFace face1 = new AFR_FSDKFace();
        AFR_FSDKFace face2 = new AFR_FSDKFace();
        face1.setFeatureData(faceData1);
        face2.setFeatureData(faceData2);
        afr_fsdkEngine.AFR_FSDK_FacePairMatching(face1, face2, score);

        return score.getScore();
    }

    // 人脸搜索
    public void faceSerch(byte[] faceData, List<byte[]> faceDataList, FaceSerchListener listener) {
        AFR_FSDKMatching score = new AFR_FSDKMatching();
        AFR_FSDKFace face1 = new AFR_FSDKFace();
        face1.setFeatureData(faceData);
        AFR_FSDKFace face2 = new AFR_FSDKFace();
        int positon = 0;
        float max = 0.0f;
        for (int i = 0; i < faceDataList.size(); i++) {
            float like = 0.0f;
            face2.setFeatureData(faceDataList.get(i));
            afr_fsdkEngine.AFR_FSDK_FacePairMatching(face1, face2, score);
            like = score.getScore();
            if (like > max) {
                max = like;
                positon = i;
            }
        }
        listener.serchFinish(max, positon);
    }


    public void destroyEngine() {
        afr_fsdkEngine.AFR_FSDK_UninitialEngine();
    }


    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

}
