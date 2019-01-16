package com.szitu.cyjl.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.szitu.cyjl.Activity.Knowledge;
import com.szitu.cyjl.Activity.Look_Bazzar;
import com.szitu.cyjl.Activity.Look_Farm;
import com.szitu.cyjl.Activity.Look_crop;
import com.szitu.cyjl.Activity.Look_man;
import com.szitu.cyjl.Activity.Serach_Market;
import com.szitu.cyjl.Activity.ShowNew;
import com.szitu.cyjl.Activity.Weather;
import com.szitu.cyjl.Adapter.Top_home_adapter;
import com.szitu.cyjl.JavaBean.Top_list_bean;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.MyNew;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class Top_Home_fragment extends Fragment implements OnBannerListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "三";
    private Banner mBanner;
    private ArrayList<String> path_list;
    private ArrayList<String> title_list;
    private MyImageLoader mMyImageLoader;
    private ListView listView;
    private View header_View;
    private Top_home_adapter adapter;
    private TextView yuyue,xuexi,hangqing,shucai,fujin,shibie,shichang,youhui;
    private ArrayList<MyNew> data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_top_home,container,false);
        Bmob.initialize(getActivity(), "900b2dcf667a67b3403f4953c227c0cb");
        initView(view);
        return view;
    }
    private void initView(View view){
        header_View=View.inflate(getActivity(),R.layout.list_header,null);
        mBanner=(Banner)header_View.findViewById(R.id.list_banner);
        yuyue = (TextView)header_View.findViewById(R.id.main_yuyue);
        yuyue.setOnClickListener(this);
        xuexi = (TextView)header_View.findViewById(R.id.main_xuexi);
        xuexi.setOnClickListener(this);
        hangqing = (TextView)header_View.findViewById(R.id.main_hangqing);
        hangqing.setOnClickListener(this);
        shucai = (TextView)header_View.findViewById(R.id.main_shucai );
        shucai.setOnClickListener(this);
        fujin = (TextView)header_View.findViewById(R.id.main_fujin);
        fujin.setOnClickListener(this);
        shichang  = (TextView)header_View.findViewById(R.id.main_shichang);
        shichang.setOnClickListener(this);
        shibie = (TextView)header_View.findViewById(R.id.main_shibie);
        shibie.setOnClickListener(this);
        youhui = (TextView)header_View.findViewById(R.id.main_youhui);
        youhui.setOnClickListener(this);
        listView=(ListView)view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        Log.d(TAG, "initView: ");
        queryData();

        listView.addHeaderView(header_View);
        selectData();
    }
    public void selectData(){
        BmobQuery query =new BmobQuery("new");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if(e==null){
                    Log.d(TAG, "done: "+ary);
                    initjson(ary);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    public void queryData(){
        BmobQuery query =new BmobQuery("slideshow");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if(e==null){
                    Json(ary);
                }else{
                    Log.i(TAG,"失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    private void initBanner(ArrayList<String> path,ArrayList<String> title){
        mMyImageLoader = new MyImageLoader();
        //设置样式，里面有很多种样式可以自己都看看效果
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(mMyImageLoader);
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        //轮播图片的文字
        mBanner.setBannerTitles(title);
        //设置轮播间隔时间
        mBanner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载地址
        mBanner.setImages(path)
                //轮播图的监听
                .setOnBannerListener(this)
                //开始调用的方法，启动轮播图。
                .start();
    }
    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getActivity(),"你点击了"+title_list.get(position),Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_yuyue:
                startActivity(new Intent(getActivity(),Look_Farm.class));
                break;
            case R.id.main_xuexi:
                Intent intent = new Intent(getActivity(),Knowledge.class);
                intent.putExtra("data","1");
                startActivity(intent);
                break;
            case R.id.main_hangqing:
                startActivity(new Intent(getActivity(),Serach_Market.class));
                break;
            case R.id.main_shucai:
                startActivity(new Intent(getActivity(),Look_man.class));
                break;
            case R.id.main_fujin:
                startActivity(new Intent(getActivity(),Look_Bazzar.class));
                break;
            case R.id.main_shibie:
                Intent intent1 = new Intent(getActivity(),Knowledge.class);
                intent1.putExtra("data","2");
                startActivity(intent1);
                break;
            case R.id.main_shichang:
                startActivity(new Intent(getActivity(),Look_crop.class));
                break;
            case R.id.main_youhui:
                startActivity(new Intent(getActivity(),Weather.class));
                break;
                default:break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyNew m = data.get(position-1);
        Intent intent =new Intent(getActivity(),ShowNew.class);
        intent.putExtra("data",m);
        startActivity(intent);
    }

    class MyImageLoader extends ImageLoader{
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext()).load((String) path).into(imageView);
        }
    }
    private void Json(JSONArray array){
        try {
            path_list=new ArrayList<>();
            title_list=new ArrayList<>();
            for(int i=0;i<array.length();i++){
                JSONObject object=array.getJSONObject(i);
                String name=object.getString("name");
                String photo=object.getString("photo");
                JSONObject jsonObject=new JSONObject(photo);
                String url=jsonObject.getString("url");
                path_list.add(url);
                title_list.add(name);
            }
            initBanner(path_list,title_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void toast(String content){
        Toast.makeText(getActivity(),content,Toast.LENGTH_SHORT).show();
    }
    private void initjson(JSONArray array){
        ArrayList<MyNew> list = new ArrayList<>();
        for(int i=0;i<array.length();i++){
            try {
                MyNew myNew =new MyNew();
                JSONObject jsonObject = array.getJSONObject(i);
                String come = jsonObject.getString("come");
                myNew.setCome(come);
                String content = jsonObject.getString("content");
                myNew.setContent(content);
                String date = jsonObject.getString("date");
                myNew.setDate(date);
                String name = jsonObject.getString("name");
                myNew.setName(name);
                String property = jsonObject.getString("property");
                myNew.setProperty(property);
                String icon = jsonObject.getString("icon");
                JSONObject object = new JSONObject(icon);
                String url = object.getString("url");
                myNew.setUrl(url);
                list.add(myNew);
                Log.d(TAG, "initjson: "+come+" "+content+" "+date+ " "+name+" "+property+" "+url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        init(list);

    }
    private void init(ArrayList<MyNew> myNews){
        data = myNews;
        adapter=new Top_home_adapter(getActivity(),R.layout.top_list_home,myNews);
        listView.setAdapter(adapter);
    }
}

