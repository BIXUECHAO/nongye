package com.szitu.cyjl.Json;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Photo_json {

    public static String[] getTitle(JSONObject object){
        String data[]=null;
        try {
            data=new String[3];
            String j = object.getString("result");
            JSONArray array=new JSONArray(j);
            for(int i=0;i<array.length();i++){
                JSONObject jsonObject=array.getJSONObject(i);
                String a=jsonObject.getString("baike_info");
                String name=jsonObject.getString("keyword");
                data[0]=name;
                String root=jsonObject.getString("root");
                data[1]=root;
                JSONObject jb=new JSONObject(a);
                String content=jb.getString("description");
                data[2]=content;
                if(content!=null && name !=null){
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
