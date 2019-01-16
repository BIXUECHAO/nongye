package com.szitu.cyjl.JavaBean;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class Activity_Manage {
    private static  List<Activity> list=new ArrayList<>();
    public static void addActivity(Activity activity){
        list.add(activity);
    }
    public static void removeActivity(Activity activity){
        list.remove(activity);
    }
    public static void clerActivity(){
        for(Activity activity:list){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        list.clear();
    }
}
