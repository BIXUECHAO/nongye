package com.szitu.cyjl.Json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Httpjsonutil {
    public static String[] getID(String json){
        String []s = new String[16];
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("HeWeather6");
            for(int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);

                String basic = object.getString("basic");

                JSONObject ob = new JSONObject(basic);
                String id = ob.getString("cid");
                s[0] = id;
                String location = ob.getString("location");
                s[1] = location;

                String update = object.getString("update");
                JSONObject ob2 = new JSONObject(update);
                String loc = ob2.getString("loc");
                s[2] = loc;
                String utc = ob2.getString("utc");
                s[3] =  utc;

                String now = object.getString("now");
                JSONObject ob3 = new JSONObject(now);
                String cloud = ob3.getString("cloud");
                s[4] = cloud;
                String cond_code = ob3.getString("cond_code");
                s[5] = cond_code;
                String cont_txt = ob3.getString("cont_txt");
                s[6] = cont_txt;
                String fl =ob3.getString("fl");
                s[7] = fl;
                String hum = ob3.getString("hum");
                s[8] = hum;
                String pcpn = ob3.getString("pcpn");
                s[9] = pcpn;
                String pres = ob3.getString("pres");
                s[10] = pres;
                String tmp = ob3.getString("tmp");
                s[11] = tmp;
                String vis = ob3.getString("vis");
                s[12] = vis;
                String wind_deg = ob3.getString("wind_deg");
                s[13] = wind_deg;
                String wind_dir = ob3.getString("wind_dir");
                s[14] = wind_dir;
                String wind_sc = ob3.getString("wind_sc");
                s[15] = wind_sc;
                String wind_spd = ob3.getString("wind_spd");
                s[16] = wind_spd;
                return s;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
