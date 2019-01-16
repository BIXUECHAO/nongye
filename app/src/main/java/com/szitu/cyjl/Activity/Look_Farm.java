package com.szitu.cyjl.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.szitu.cyjl.Adapter.Look_Adapter;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.Subscribe;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class Look_Farm extends AppCompatActivity implements OnBannerListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private static final String TAG = "Look_Farm";
    private ListView listView;
    private View header_View;
    private Banner mBanner;
    private MyImageLoader mMyImageLoader;
    private ArrayList<String> path_list;
    private ArrayList<String> title_list;
    private ArrayList<String> datalist;
    private Toolbar toolbar;
    private EditText et_search;
    private ImageView iv_search;
    private TextView screen;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EditText screen_shen,screen_shi,screen_qu,screen_type,screen_min,screen_max;
    private Button screen_submit,screen_reset;
    private SwipeRefreshLayout swipeRefreshLayout;
    List<Subscribe> data;
    private Look_Adapter adapter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look__farm);
        initView();
        queryData();
        dialog();
        selectData(null,null,null,"0","99999999");
    }
    private void initView(){
        header_View=View.inflate(Look_Farm.this,R.layout.list_look_farm_header,null);
        mBanner=(Banner)header_View.findViewById(R.id.look_list_banner);
        toolbar = (Toolbar)header_View.findViewById(R.id.look_header_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        et_search = (EditText)header_View.findViewById(R.id.look_et_search);
        iv_search = (ImageView)header_View.findViewById(R.id.look_iv_search);
        iv_search.setOnClickListener(this);
        screen = (TextView)header_View.findViewById(R.id.look_screen);
        drawerLayout = (DrawerLayout)findViewById(R.id.look_drawerlayout);
        navigationView = (NavigationView)findViewById(R.id.look_navigation);
        screen.setOnClickListener(this);
        listView = (ListView)findViewById(R.id.look_listView);
        listView.addHeaderView(header_View);
        listView.setOnItemClickListener(this);
        View rightView = navigationView.inflateHeaderView(R.layout.look_farm_screen);
        screen_shen = (EditText)rightView.findViewById(R.id.screen_sheng);
        screen_shi = (EditText)rightView.findViewById(R.id.screen_city);
        screen_qu = (EditText)rightView.findViewById(R.id.screen_qu);
        screen_type = (EditText)rightView.findViewById(R.id.screen_type);
        screen_min = (EditText)rightView.findViewById(R.id.screen_min);
        screen_max = (EditText)rightView.findViewById(R.id.screen_max);
        screen_submit = (Button)findViewById(R.id.screen_submit);
        screen_submit.setOnClickListener(this);
        screen_reset = (Button)findViewById(R.id.screen_reset);
        screen_reset.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.look_swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.MaterialRed);
        swipeRefreshLayout.setOnRefreshListener(this);
    }
    private void initBanner(ArrayList<String> path, ArrayList<String> title){
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
                Log.d(TAG, "Json: "+url);
                path_list.add(url);
                title_list.add(name);
            }
            initBanner(path_list,title_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.look_iv_search:
                CharSequence text = et_search.getText();
                if(TextUtils.isEmpty(text)){
                    return;
                }
                String search = et_search.getText().toString();
                selectData(null,search,null,"0","999999");
                dialog();
                break;
            case R.id.look_screen:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.screen_submit:
                String local =null;
                String shen = screen_shen.getText().toString();
                String shi = screen_shi.getText().toString();
                String qu =screen_qu.getText().toString();
                String npname = screen_type.getText().toString();
                String min = screen_min.getText().toString();
                String max = screen_max.getText().toString();
                CharSequence m1 = screen_min.getText();
                CharSequence m2 = screen_max.getText();
                if(TextUtils.isEmpty(m1)){
                    min = "0";
                }
                if(TextUtils.isEmpty(m2)){
                    max = "999999";
                }
                if(shen.equals("") || shi.equals("")||qu.equals("")){
                    local = null;
                }else {
                    local = shen+shi+qu;
                }
                if(npname.equals("")){
                    npname = null;
                }
                selectData(local,null,npname,min,max);
                dialog();
                break;
            case R.id.screen_reset:
                screen_shen.setText("");
                screen_shi.setText("");
                screen_qu.setText("");
                screen_type.setText("");
                screen_min.setText("");
                screen_max.setText("");
                break;
                default:break;
        }
    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getApplicationContext(),"你点击了"+title_list.get(position),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        selectData(null,null,null,"0","99999999");
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(data.size() > 0 && data != null){
            Subscribe subscribe = data.get(position-1);
            Log.d(TAG, "onItemClick: 长度"+data.size()+"   "+position);
            Intent intent = new Intent(Look_Farm.this,Subscrebe_item.class);
            intent.putExtra("data",subscribe);
            startActivity(intent);
        }
    }

    class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext()).load((String) path).into(imageView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
                default:break;
        }
        return true;
    }
    private void toast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT).show();
    }
    private void selectData(String local,String content,String name,String min,String max){
        Log.d(TAG, "selectData: ");
        BmobQuery<Subscribe> querylocal = new BmobQuery<Subscribe>();
        querylocal.addWhereEqualTo("local",local);
        BmobQuery<Subscribe> queryType = new BmobQuery<>();
        queryType.addWhereEqualTo("type","农机");
        BmobQuery<Subscribe> queryName = new BmobQuery<>();
        queryName.addWhereEqualTo("sp_name",name);
        BmobQuery<Subscribe> queryMin = new BmobQuery<>();
        queryMin.addWhereGreaterThanOrEqualTo("min", min);
        BmobQuery<Subscribe> queryMax = new BmobQuery<>();
        queryMax.addWhereLessThanOrEqualTo("min", max);
        BmobQuery<Subscribe> queryContent = new BmobQuery<>();
        queryContent.addWhereEqualTo("introduce",content);
        List<BmobQuery<Subscribe>> queryList =new ArrayList<>();
        queryList.add(querylocal);
        queryList.add(queryType);
        queryList.add(queryName);
        queryList.add(queryMin);
        queryList.add(queryMax);
        queryList.add(queryContent);
        BmobQuery<Subscribe> query = new BmobQuery<>();
        query.and(queryList);
        query.findObjects(new FindListener<com.szitu.cyjl.bmob.Subscribe>() {
            @Override
            public void done(List<Subscribe> object, BmobException e) {
                if(e==null){
                    Log.d(TAG, "done: "+object.size());
                    initList(object);
                    data = object;
                    progressDialog.dismiss();
                }else{
                    Log.d(TAG, "done: 失败");
                    progressDialog.dismiss();
                    Toast.makeText(Look_Farm.this,"刷新失败,请检查网络",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void initList(List<Subscribe> list){
        adapter = new Look_Adapter(Look_Farm.this,R.layout.list_look_item,list);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
    private void dialog(){
        progressDialog=new ProgressDialog(Look_Farm.this);
        progressDialog.setMessage("正在查找...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
