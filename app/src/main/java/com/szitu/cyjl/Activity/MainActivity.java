package com.szitu.cyjl.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.szitu.cyjl.BasedActivity.BasedActivity;
import com.szitu.cyjl.Fragment.Last_Forum_fragment;
import com.szitu.cyjl.Fragment.Last_Home_fragment;
import com.szitu.cyjl.Fragment.Last_Query_fragment;
import com.szitu.cyjl.Fragment.Last_Sunscribe_fragment;
import com.szitu.cyjl.Fragment.Last_Xiaoxi_fragment;
import com.szitu.cyjl.JavaBean.Activity_Manage;
import com.szitu.cyjl.JavaBean.Voice;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.User;

import org.apache.log4j.chainsaw.Main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends BasedActivity implements AMapLocationListener {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private TextView tb_text,tb_local;
    private RadioGroup radioGroup;
    private FrameLayout frameLayout;
    private RadioButton rb_home,rb_forum,rb_query,rb_subscribe,rb_xinxi;
    private long fristtime=0;
    private Last_Home_fragment home_fragment;
    private Last_Forum_fragment forum_fragment;
    private Last_Query_fragment query_fragment;
    private Last_Sunscribe_fragment subscribe_fragment;
    private Last_Xiaoxi_fragment xiaoxi_fragment;
    private SpeechSynthesizer mTts;
    private FloatingActionButton floatingActionButton;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "900b2dcf667a67b3403f4953c227c0cb");
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"=5be7dde3");
        setContentView(R.layout.activity_main);
        initView();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tb_text.setText("首页");
        initListener();
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.more);
        }
        List<String> primissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            primissionList.add(Manifest.permission.CAMERA);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            primissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            primissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            primissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            primissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            primissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            primissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(!primissionList.isEmpty()){
            String []primissions = primissionList.toArray(new String[primissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,primissions,1);
        }
        rb_home.setChecked(true);
        replace_fragment(home_fragment);
//        initnavigationView();
        BmobUser bmobUser = BmobUser.getCurrentUser();
        mTts = SpeechSynthesizer.createSynthesizer(this, null);
        set_mTts();
        location();
    }
    private void initView(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        //toolbar上的文字
        tb_text=(TextView)findViewById(R.id.tb_text);

        tb_local = (TextView)findViewById(R.id.tb_local);

        radioGroup=(RadioGroup)findViewById(R.id.radiogroup);
        //主页
        rb_home=(RadioButton)findViewById(R.id.rb_home);
        //小游戏
        rb_forum=(RadioButton)findViewById(R.id.rb_forum);
        //历史
        rb_query=(RadioButton)findViewById(R.id.rb_query);

        rb_xinxi = (RadioButton)findViewById(R.id.rb_xinxi);

        rb_subscribe=(RadioButton)findViewById(R.id.rb_subscribe);

        frameLayout=(FrameLayout)findViewById(R.id.fragment_layout);

        floatingActionButton = (FloatingActionButton)findViewById(R.id.float_yuyin);

        sharedPreferences=getSharedPreferences("local",MODE_PRIVATE);
        home_fragment=new Last_Home_fragment();
        forum_fragment=new Last_Forum_fragment();
        query_fragment=new Last_Query_fragment();
        subscribe_fragment=new Last_Sunscribe_fragment();
        xiaoxi_fragment = new Last_Xiaoxi_fragment();
    }
    private void initListener(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        replace_fragment(home_fragment);
                        tb_text.setText("首页");
                        break;
                    case R.id.rb_forum:
                        replace_fragment(subscribe_fragment);
                        tb_text.setText("附近");
                        break;
                    case R.id.rb_query:
                        replace_fragment(query_fragment);
                        tb_text.setText("关注");
                        break;
                    case R.id.rb_xinxi:
                        replace_fragment(xiaoxi_fragment);
                        tb_text.setText("消息");
                        break;
                    case R.id.rb_subscribe:
                        replace_fragment(forum_fragment);
                        tb_text.setText("个人中心");
                        break;
                        default:break;
                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech();
            }
        });
//        left_headerlocal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,MyMap.class);
//                startActivity(intent);
//            }
//        });
        tb_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MyMap.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:
                Toast.makeText(getApplicationContext(),"帮助文档",Toast.LENGTH_LONG).show();
                break;
                default:break;
        }
        return true;
    }
//    private void initnavigationView(){
//        navigationView.setCheckedItem(R.id.myrelease);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.myrelease:
//                        Intent intent=new Intent(MainActivity.this,Release.class);
//                        startActivity(intent);
//                        break;
//                    case R.id.mysubscribe:
//                        Intent intent1=new Intent(MainActivity.this,Subscribe.class);
//                        startActivity(intent1);
//                        break;
//                    case R.id.my:
//                        Intent intent2=new Intent(MainActivity.this,My.class);
//                        startActivity(intent2);
//                        break;
//                    case R.id.logout:
//                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this)
//                                .setTitle("确定注销登录？")
//                                .setMessage("注销后需重新登录，方可使用应用")
//                                .setCancelable(false)
//                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                })
//                                .setNegativeButton("注销", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        BmobUser.logOut();
//                                        Activity_Manage.clerActivity();
//                                        Intent intent3=new Intent(MainActivity.this,Logn.class);
//                                        startActivity(intent3);
//                                        Toast.makeText(getApplicationContext(),"注销成功",Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                        builder.show();
//                        break;
//                    case R.id.quit:
//                        Activity_Manage.clerActivity();
//                        break;
//                        default:break;
//                }
//                return true;
//            }
//        });
//    }
    private void replace_fragment(Fragment fragment){
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.fragment_layout,fragment);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        long secondtime=System.currentTimeMillis();
        if(secondtime-fristtime > 2000){
            Toast.makeText(getApplicationContext(),"再按一次退出应用",Toast.LENGTH_SHORT).show();
            fristtime=secondtime;
        }else {
            Activity_Manage.clerActivity();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result:grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(getApplicationContext(),"请到设置中打开权限",Toast.LENGTH_LONG).show();
                        }else {

                        }
                    }
                }
                break;
                default:break;
        }
    }
    private void speech(){
        //创建语音识别对话框
        RecognizerDialog rd = new RecognizerDialog(MainActivity.this,null);
        //设置参数accent,language等参数
        rd.setParameter(SpeechConstant.LANGUAGE,"zh_cn");//中文
        rd.setParameter(SpeechConstant.ACCENT,"mandarin");//普通话
        //设置回调接口
        rd.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                //获取返回结果
                String  result = recognizerResult.getResultString();
                Log.d(TAG,result+"  "+b);
                if(!b){
                    Log.d(TAG, "onResult:========= "+result);
                    parseVoice(result);
                }
                //parseVoice(result);
            }
            @Override
            public void onError(SpeechError speechError) {

            }
        });
        //显示对话框
        rd.show();
        TextView txt = (TextView)rd.getWindow().getDecorView().findViewWithTag("textlink");
        txt.setText("一代农");
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void parseVoice(String resultString) {
        Gson gson = new Gson();
        Voice voiceBean = gson.fromJson(resultString, Voice.class);
        StringBuffer sb = new StringBuffer();
        ArrayList<Voice.WSBean> ws = voiceBean.ws;
        for (Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        Toast.makeText(getApplicationContext(),sb.toString(),Toast.LENGTH_LONG).show();
        Myspeech(sb.toString());
    }
    private void set_mTts() {
        // 设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");

        // 设置语速
        mTts.setParameter(SpeechConstant.SPEED, "20");

        // 设置音调
        mTts.setParameter(SpeechConstant.PITCH, "50");

        // 设置音量0-100
        mTts.setParameter(SpeechConstant.VOLUME, "100");

        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 如果不需要保存保存合成音频，请注释下行代码
        // mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
        // "./sdcard/iflytek.pcm");

    }
    private void hecheng(String content){
        mTts.startSpeaking(content, mTtsListener);
    }
    private void Myspeech(String myspeech){
        CharSequence text = myspeech;
        if(TextUtils.isEmpty(text)){
            return;
        }
        else if(fanhui(myspeech,"你好")){
            hecheng("你好,欢迎为您服务");
        }
        else if(fanhui(myspeech,"找") && fanhui(myspeech,"价格")){
           int a = myspeech.indexOf("找");
           int b = myspeech.indexOf("的价格");
           String c = myspeech.substring(a+1,b);
            Log.d("找广告", "Myspeech: "+c);
            Intent intent = new Intent(MainActivity.this,Show_Market_Data.class);
            intent.putExtra("name",c);
            startActivity(intent);
        }else if(fanhui(myspeech,"找农机")){
            startActivity(new Intent(MainActivity.this,Look_Farm.class));
        }else if(fanhui(myspeech,"找人")){
            startActivity(new Intent(MainActivity.this,Look_man.class));
        }else if(fanhui(myspeech,"农作物")){
            startActivity(new Intent(MainActivity.this,Look_crop.class));
        }
        else if(fanhui(myspeech,"关闭") && fanhui(myspeech,"应用") || fanhui(myspeech,"软件")){
            Activity_Manage.clerActivity();
        }else if(fanhui(myspeech,"打开") && fanhui(myspeech,"个人")){
            rb_subscribe.setChecked(true);
        }else if(fanhui(myspeech,"打开") && fanhui(myspeech,"主页")){
            rb_home.setChecked(true);
        }else if(fanhui(myspeech,"打开") && fanhui(myspeech,"关注")){
            rb_query.setChecked(true);
        }else if(fanhui(myspeech,"打开") && fanhui(myspeech,"附近")){
            rb_forum.setChecked(true);
        }else if(fanhui(myspeech,"打开") && fanhui(myspeech,"信息")){
            rb_xinxi.setChecked(true);
        }
        else {
            hecheng("我不明白你的意思");
        }

    }
    private boolean fanhui(String a,String b){
        return a.contains(b);
    }
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
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
        Log.d(TAG, "onLocationChanged: ");
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
                final String qu = amapLocation.getDistrict();
                selectlocal(amapLocation.getProvince(),amapLocation.getCity(),amapLocation.getDistrict(),amapLocation.getStreet(),myadress);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tb_local.setText(qu);
                    }
                });
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.d(TAG,"location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
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
