package com.szitu.cyjl.bmob;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.szitu.cyjl.Activity.HandMessage;
import com.szitu.cyjl.Activity.SendMessage;
import com.szitu.cyjl.JavaBean.Msg;
import com.szitu.cyjl.R;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

public class bmobMessageHandler extends BmobIMMessageHandler {
    private static final String TAG = "bmobMessageHandler";

    private Context context;
    public bmobMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceive(final MessageEvent event) {
        String objectid=event.getFromUserInfo().getUserId();
        String message=event.getMessage().getContent();
        String name=event.getFromUserInfo().getName();
        Log.d(TAG, "onMessageReceive: "+message);
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMessageReceive: "+objectid+"  "+name);
//        Bundle bundle = new Bundle();
//        bundle.putString("receiveMemberID", String.valueOf(chatObjectID) );
//        bundle.putString("receiveMemberName", chatObjectName);
//        bundle.putString("receiveMemberAvator", chatObjectAvator);

        Intent intent = new Intent(context,HandMessage.class);
        intent.putExtra("userObjectID",objectid);
        intent.putExtra("username",name);
        intent.putExtra("MSG",message);

        PendingIntent pi = PendingIntent.getActivity(context,0,intent,0);
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("新消息")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pi)
                .build();
        manager.notify(1,notification);
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
    }

}
