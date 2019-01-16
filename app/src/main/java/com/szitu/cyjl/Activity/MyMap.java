package com.szitu.cyjl.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.szitu.cyjl.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.b.V;

public class MyMap extends AppCompatActivity implements AMapLocationListener {
    private static final String TAG = "MyMap";
    private MapView mapView;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private TextView myLocalhost,alter;

    private EditText at_shen,at_city,at_local;
    private boolean is = true;
    private AMap aMap;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_map);
        initView(savedInstanceState);
        initLintener();
        location();
    }
    private void  initView(Bundle saved){
        mapView = (MapView)findViewById(R.id.mapView);
        mapView.onCreate(saved);
        aMap = mapView.getMap();
        myLocalhost = (TextView) findViewById(R.id.mylocalhost);
        alter = (TextView)findViewById(R.id.map_alert);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        sharedPreferences=getSharedPreferences("local",MODE_PRIVATE);
    }
    private void initLintener(){
        alter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertlog();
            }
        });

    }
    private void alertlog(){
        View view = View.inflate(MyMap.this,R.layout.alter_local,null);
        at_shen = (EditText) view.findViewById(R.id.at_shen);
        at_city = (EditText) view.findViewById(R.id.at_city);
        at_local = (EditText) view.findViewById(R.id.at_local);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("修改地址")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myLocalhost.setText(at_shen.getText().toString()+at_city.getText().toString()+at_local.getText().toString());
                        selectlocal(at_shen.getText().toString(),at_city.getText().toString(),at_local.getText().toString(),null,null);
                    }
                })
                .setNegativeButton("取消",null);
        builder.show();
    }
    private void location(){
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000*60*5);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double a=amapLocation.getLatitude();//获取纬度
                Log.d(TAG, "onLocationChanged: 纬度   "+a);
                double b=amapLocation.getLongitude();//获取经度
                Log.d(TAG, "onLocationChanged:  经度  "+b);
                float c=amapLocation.getAccuracy();//获取精度信息
                Log.d(TAG, "onLocationChanged: 精度信息  "+c);

                Log.d(TAG, "onLocationChanged: 我的位置"+amapLocation.getAddress());
                final String myadress = amapLocation.getAddress();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myLocalhost.setText(myadress);
                    }
                });
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.d(TAG,"location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    private void selectlocal(String province,String city,String district,String street,String myadress){
        editor = sharedPreferences.edit();
        editor.putString("province",province);
        editor.putString("city",city);
        editor.putString("district",district);
        editor.putString("street",street);
        editor.putString("mylocal",myadress);
        editor.commit();
        Log.d(TAG, "selectlocal: 成功");
    }
}
