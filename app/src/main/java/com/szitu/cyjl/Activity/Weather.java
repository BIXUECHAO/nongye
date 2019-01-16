package com.szitu.cyjl.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.szitu.cyjl.Json.Httpjsonutil;
import com.szitu.cyjl.R;
import com.szitu.cyjl.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Weather extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "Weather";
    private SharedPreferences sharedPreferences;
    private String city;
    private String qu;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    private ImageView infoImg;
    public SwipeRefreshLayout swipeRefreshLayout;
    private Button navButon;
    private ImageView iv_icon;
    private TextView zhuangtai,wendu;
    private TextView jiangshui,yaqiang,fengli,fengxiang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_weather);
        initView();
        initData();
        loadBingPic();
    }
    private void initView(){
        sharedPreferences = getSharedPreferences("local",MODE_PRIVATE);
        city = sharedPreferences.getString("city","苏州");
        qu = sharedPreferences.getString("district","吴江");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        navButon = (Button) findViewById(R.id.nav_button);
        zhuangtai = (TextView)findViewById(R.id.zhuangtai);
        wendu = (TextView)findViewById(R.id.wendu);
        jiangshui = (TextView)findViewById(R.id.jiangshuiliang);
        yaqiang = (TextView)findViewById(R.id.yaqiang);
        fengli = (TextView)findViewById(R.id.fengli);
        fengxiang = (TextView)findViewById(R.id.fengxiang);
        iv_icon = (ImageView)findViewById(R.id.iv_icon_tianqi);
    }
    private void initData(){
        //开启子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String d =  HttpUtil.getData(getweather(city));
                Log.d(TAG, "run: "+d);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getID(d);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    private String getweather(String city){
        String u = "https://free-api.heweather.net/s6/weather/now?location="+city+"&key=e1e2d8cf3c2b4192b5b70a1d45c85462";
        return u;
    }
    private String getweatherID(String id){
        String weatherurl = "http://guolin.tech/api/weather?cityid=" + id + "&key=e1e2d8cf3c2b4192b5b70a1d45c85462";
        return weatherurl;
    }
    private void toast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
    }
    private void loadBingPic() {
        final String requestBingPic = "http://guolin.tech/api/bing_pic";
        new Thread(new Runnable() {
            @Override
            public void run(){
                final String j = HttpUtil.getData(requestBingPic);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(Weather.this).load(j).into(bingPicImg);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    public void getID(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("HeWeather6");
            for(int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                String basic = object.getString("basic");
                JSONObject ob = new JSONObject(basic);
                String id = ob.getString("cid");

                Log.d(TAG, "getID: 0"+id);
                String location = ob.getString("location");
                titleCity.setText(location);
                //titleCity.setText(city+qu);
                Log.d(TAG, "getID:1 "+location);
                String update = object.getString("update");
                JSONObject ob2 = new JSONObject(update);
                String loc = ob2.getString("loc");

                Log.d(TAG, "getID: 2"+loc);
                String utc = ob2.getString("utc");
                titleUpdateTime.setText(utc);
                Log.d(TAG, "getID:3 "+utc);
                String now = object.getString("now");
                JSONObject ob3 = new JSONObject(now);
                //云量
                String cloud = ob3.getString("cloud");

                Log.d(TAG, "getID: 4"+cloud);
                //实况天气状况代码
                String cond_code = ob3.getString("cond_code");

                Log.d(TAG, "getID: 5"+cond_code);
                //实况天气状况描述
                String cond_txt = ob3.getString("cond_txt");
                weatherInfoText.setText(cond_txt);
                zhuangtai.setText(cond_txt);
                setPic(cond_txt);
                Log.d(TAG, "getID: 6"+cond_txt);
                //体感温度，默认单位：摄氏度
                String fl =ob3.getString("fl");
                degreeText.setText(fl+"℃");
                wendu.setText(fl);
                Log.d(TAG, "getID: 7"+fl);
                //相对湿度
                String hum = ob3.getString("hum");
                aqiText.setText(hum);
                Log.d(TAG, "getID: 8"+hum);
                //降水量
                String pcpn = ob3.getString("pcpn");
                jiangshui.setText(pcpn);
                Log.d(TAG, "getID:9 "+pcpn);
                //大气压强
                String pres = ob3.getString("pres");
                yaqiang.setText(pres);
                Log.d(TAG, "getID: 10"+pres);
                //温度，默认单位：摄氏度
                String tmp = ob3.getString("tmp");

                Log.d(TAG, "getID: 11"+tmp);
                //能见度，默认单位：公里
                String vis = ob3.getString("vis");
                pm25Text.setText(vis);
                Log.d(TAG, "getID: 12"+vis);
                //风向360角度
                String wind_deg = ob3.getString("wind_deg");

                Log.d(TAG, "getID: 13"+wind_deg);
                //风向
                String wind_dir = ob3.getString("wind_dir");
                fengxiang.setText(wind_dir);
                Log.d(TAG, "getID: 14"+wind_dir);
                //风力
                String wind_sc = ob3.getString("wind_sc");
                fengli.setText(wind_sc);
                Log.d(TAG, "getID: 15"+wind_sc);
                //风速，公里/小时
                String wind_spd = ob3.getString("wind_spd");

                Log.d(TAG, "getID: 16"+wind_spd);

                //return s;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //return null;
        }
        //return null;
    }
    private void setPic(String forecast_info) {
        switch (forecast_info) {
            case "晴":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img0));
                break;
            case "多云":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img4));
                break;
            case "晴间多云":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img5));
                break;
            case "晴大部多云":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img7));
                break;
            case "阴":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img9));
                break;
            case "阵雨":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img10));
                break;
            case "雷阵雨":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img11));
                break;
            case "雷阵雨伴冰雹":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img12));
                break;
            case "小雨":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img13));
                break;
            case "中雨":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img14));
                break;
            case "大雨":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img15));
                break;
            case "暴雨":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img16));
                break;
            case "大暴雨":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img17));
                break;
            case "特大暴雨":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img18));
                break;
            case "冻雨":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img19));
                break;
            case "雨夹雪":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img20));
                break;
            case "阵雪":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img21));
                break;
            case "小雪":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img22));
                break;
            case "中雪":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img23));
                break;
            case "大雪":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img24));
                break;
            case "暴雪":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img25));
                break;
            case "浮尘":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img26));
                break;
            case "扬沙":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img27));
                break;
            case "沙尘暴":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img28));
                break;
            case "强沙尘暴":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img29));
                break;
            case "雾":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img30));
                break;
            case "霾":
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img31));
                break;
            default:
                iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img39));
                break;
        }
    }
    @Override
    public void onRefresh() {
        initData();
        loadBingPic();
    }
}
