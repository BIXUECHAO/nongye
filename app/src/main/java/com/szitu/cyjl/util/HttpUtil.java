package com.szitu.cyjl.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {

    public static String getData(String u){
        InputStream in = null;
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(u);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            try {
                connection.setConnectTimeout(5000);
            }catch (Exception e){
                return "连接超时";
            }
            connection.setRequestMethod("GET");
            int code = connection.getResponseCode();
            if(code == 200){
                in = connection.getInputStream();
                byte []b = new byte[2048];
                int a=-1;
                while ((a=in.read(b)) != -1){
                    String s = new String(b,0,a);
                    buffer.append(s);
                }
                return buffer.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
