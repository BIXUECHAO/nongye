package com.szitu.cyjl.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.szitu.cyjl.Activity.FaceRegisterActivity;
import com.szitu.cyjl.Activity.Rrelease_Subscribe;
import com.szitu.cyjl.Activity.Subscrebe_item;
import com.szitu.cyjl.Adapter.Subscribe_adapter;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Last_Sunscribe_fragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView listView;
    private SharedPreferences sharedPreferences;
    private String mylocal;
    private Subscribe_adapter adapter;
    private static final String TAG = "Last_Sunscribe_fragment";
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    List<Subscribe> data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_last_subscribe,container,false);
        initView(view);
        return view;
    }
    private void initView(View view){
        listView=(ListView)view.findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swip_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.zhuti);
        swipeRefreshLayout.setOnRefreshListener(this);
        sharedPreferences = getActivity().getSharedPreferences("local",Context.MODE_PRIVATE);
        mylocal = sharedPreferences.getString("province","江苏省");
        mylocal+=sharedPreferences.getString("city","苏州市");
        mylocal+=sharedPreferences.getString("district","吴江区");
        dialog();
        selectData(mylocal);
        listView.setOnItemClickListener(this);

    }
    private void selectData(String local){
        Log.d(TAG, "selectData: ");
        BmobQuery<Subscribe> query = new BmobQuery<Subscribe>();
        query.addWhereEqualTo("local",local);
        query.findObjects(new FindListener<Subscribe>() {
            @Override
            public void done(List<Subscribe> object, BmobException e) {
                if(e==null){
                    initList(object);
                    data = object;
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"刷新失败,请检查网络",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void initList(List<Subscribe> list){
        adapter = new Subscribe_adapter(getActivity(),R.layout.list_subscribe,list);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
    private void dialog(){
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("正在查找...");
        progressDialog.setCancelable(false);
        progressDialog.show();
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
            Subscribe subscribe = data.get(position);
            Intent intent = new Intent(getActivity(),Subscrebe_item.class);
            intent.putExtra("data",subscribe);
            startActivity(intent);
        }
    }
    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        selectData(mylocal);
    }
}
