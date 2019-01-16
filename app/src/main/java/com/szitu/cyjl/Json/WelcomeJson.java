package com.szitu.cyjl.Json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeJson {
    public static String getJson(JSONArray array){
        String data=null;
        try{
            for(int i=0;i<array.length();i++){
                JSONObject object=array.getJSONObject(i);
                String photo=object.getString("photo");
                JSONObject jsonObject=new JSONObject(photo);
                data=jsonObject.getString("url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
