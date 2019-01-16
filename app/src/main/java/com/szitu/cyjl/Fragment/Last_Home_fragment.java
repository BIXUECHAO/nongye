package com.szitu.cyjl.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.szitu.cyjl.Adapter.MyPageAdapter;
import com.szitu.cyjl.R;

import java.util.ArrayList;
import java.util.List;

import static com.szitu.cyjl.R.color.green;

public class Last_Home_fragment extends Fragment {
    private RadioGroup radioGroup;
    private RadioButton home,bazaar,knowledge;
    private ViewPager viewPager;
    private List<Fragment> list;
    private MyPageAdapter myPageAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_last_home,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initList();
        initListener();
        initViewPage();
        viewPager.setOffscreenPageLimit(1);
    }

    private void initView(View view){
        radioGroup=(RadioGroup)view.findViewById(R.id.top_radiogroup);
        home=(RadioButton)view.findViewById(R.id.top_rb_home);
        bazaar=(RadioButton)view.findViewById(R.id.top_rb_bazaar);
        knowledge=(RadioButton)view.findViewById(R.id.top_rb_knowledge);
        viewPager=(ViewPager)view.findViewById(R.id.top_viewpager);
    }
    private void initListener(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.top_rb_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.top_rb_bazaar:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.top_rb_knowledge:
                        viewPager.setCurrentItem(2);
                        break;
                        default:break;
                }
            }
        });
    }
    private void initViewPage(){
        home.setChecked(true);
        myPageAdapter=new MyPageAdapter(this.getChildFragmentManager(),list);
        viewPager.setAdapter(myPageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        home.setChecked(true);
                        break;
                    case 1:
                        bazaar.setChecked(true);
                        break;
                    case 2:
                        knowledge.setChecked(true);
                        break;
                    default:break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void initList(){
        list=new ArrayList<>();
        list.add(new Top_Home_fragment());
//        list.add(new Top_Bazaar_fragment());
//        list.add(new Top_Knowledge_fragment());
    }
}
