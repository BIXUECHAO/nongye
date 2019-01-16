package com.szitu.cyjl.Fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.szitu.cyjl.Activity.Album_Query;
import com.szitu.cyjl.Activity.PhotoQuery;
import com.szitu.cyjl.Activity.Subscribe;
import com.szitu.cyjl.R;

import java.util.ArrayList;

public class Last_Query_fragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private RecyclerView recyclerView;
    private ImageView add;
    private LineChart lineChart;
    private ArrayList<String> nameList;
    private String name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_last_query,container,false);
        initView(view);
        return view;
    }
    private void initView(View view){
        spinner = (Spinner)view.findViewById(R.id.my_spinner);
        recyclerView = (RecyclerView)view.findViewById(R.id.my_recycle);
        add = (ImageView)view.findViewById(R.id.my_add);
        lineChart = (LineChart)view.findViewById(R.id.lineChart);
        initList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,nameList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initChart();

    }
    private void initChart(){
        ArrayList<Entry> data = new ArrayList<>();
        data.add(new Entry(3,1));
        data.add(new Entry(5,2));
        data.add(new Entry(6,3));
        data.add(new Entry(4,4));
        data.add(new Entry(6,5));
        LineDataSet dataSet = new LineDataSet(data,name);
        ArrayList<String> list = new ArrayList<>();
        for(int i=1;i<=6;i++){
            list.add(i+"");
        }
        LineData lineData = new LineData(list,dataSet);
        lineChart.setData(lineData);
    }
    private void initList(){
        nameList = new ArrayList<>();
        nameList.add("大蒜");
        nameList.add("棉花");
        nameList.add("小麦");
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        name = parent.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
