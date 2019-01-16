package com.szitu.cyjl.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.szitu.cyjl.Adapter.Top_home_adapter;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.MyNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


public class Knowledge extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private static final String TAG = "Knowledge";
    private Toolbar toolbar;
    private TextView tb_text,tb_local;
    private ArrayList<MyNew> data;
    private Top_home_adapter adapter;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String leixing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        initView();
    }
    @SuppressLint("ResourceAsColor")
    private void initView() {
        leixing = getIntent().getStringExtra("data");
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.zhuti);
        swipeRefreshLayout.setOnRefreshListener(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb_text = (TextView)findViewById(R.id.tb_text);
        if(leixing.equals("1")){
            tb_text.setText("新闻动态");
        }else if(leixing.equals("2")){
            tb_text.setText("政策变化");
            toolbar.setBackgroundColor(R.color.MaterialSeaBlue);
        }
        tb_local = (TextView)findViewById(R.id.tb_local);
        tb_local.setVisibility(View.GONE);
        selectData();
    }
    public void selectData() {
        BmobQuery query = new BmobQuery("new");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    Log.d(TAG, "done: " + ary);
                    initjson(ary);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void initjson(JSONArray array) {
        ArrayList<MyNew> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                MyNew myNew = new MyNew();
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
                Log.d(TAG, "initjson: " + come + " " + content + " " + date + " " + name + " " + property + " " + url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        init(list);

    }

    private void init(ArrayList<MyNew> myNews) {
        data = myNews;
        adapter = new Top_home_adapter(Knowledge.this, R.layout.top_list_home, myNews);
        listView.setAdapter(adapter);
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        selectData();
        swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyNew m = data.get(position-1);
        Intent intent =new Intent(Knowledge.this,ShowNew.class);
        intent.putExtra("data",m);
        startActivity(intent);
    }
}
