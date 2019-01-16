package com.szitu.cyjl.Json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Weather {
    public static ArrayList getData(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            String HeWeather = jsonObject.getString("HeWeather");
            JSONArray array = new JSONArray(HeWeather);
            for(int i=0;i<array.length();i++ ){
                JSONObject object = array.getJSONObject(i);
                String basic = object.getString("basic");
                JSONObject o1 = new JSONObject(basic);
                String location = o1.getString("location");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
