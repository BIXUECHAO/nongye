package com.szitu.cyjl.Activity;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szitu.cyjl.Adapter.MsgAdapter;
import com.szitu.cyjl.JavaBean.Msg;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class SendMessage extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SendMessage";
    private Toolbar toolbar;
    private TextView tb_text,tb_local;
    private String userObjectID,username,userPhone;
    private RecyclerView msg_recycle;
    private ImageView img_file,img_send;
    private EditText et_Message;
    private LinearLayout filelayout;
    private BmobIMConversation mBmobIMConversation;
    private User user;
    private MsgAdapter adapter;
    private List<Msg> msgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        initView();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.fanhui);
        }
        Content();
    }
    private void initView() {
        userObjectID=getIntent().getStringExtra("userObjectID");
        username = getIntent().getStringExtra("username");
        userPhone = getIntent().getStringExtra("userPhone");
        Log.d(TAG, "initView: "+userObjectID+username);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tb_text = (TextView)findViewById(R.id.tb_text);
        tb_text.setText(username);
        tb_local = (TextView)findViewById(R.id.tb_local);
        tb_local.setVisibility(View.GONE);
        msg_recycle = (RecyclerView)findViewById(R.id.msg_recycleview);
        img_file = (ImageView)findViewById(R.id.img_file);
        img_file.setOnClickListener(this);
        img_send = (ImageView) findViewById(R.id.img_send);
        img_send.setOnClickListener(this);
        et_Message = (EditText)findViewById(R.id.et_message);
        filelayout = (LinearLayout)findViewById(R.id.file_layout);
        user = BmobUser.getCurrentUser(User.class);
        msgList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayout.VERTICAL);
        msg_recycle.setLayoutManager(manager);
        adapter = new MsgAdapter(msgList);
        msg_recycle.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
                default:break;
        }
        return true;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_file:
                if(filelayout.getVisibility() == View.GONE){
                    filelayout.setVisibility(View.VISIBLE);
                }else {
                    filelayout.setVisibility(View.GONE);
                }
                break;
            case R.id.img_send:
                if(filelayout.getVisibility() != View.GONE) {
                    filelayout.setVisibility(View.GONE);
                }
                CharSequence text = et_Message.getText();
                if(TextUtils.isEmpty(text)){
                    return;
                }
                initSession(userObjectID,et_Message.getText().toString(),username);
                break;
                default:break;
        }
    }
    private void toast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT).show();
    }
    private void Content(){
        if (!TextUtils.isEmpty(user.getObjectId())) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        Log.d(TAG, "done: 服务器连接成功");
                    } else {
                        Toast.makeText(getApplicationContext(),"服务器连接失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void initSession(final String userid, final String message, String name) {
        BmobIMUserInfo info =new BmobIMUserInfo();
        info.setUserId(userid);
        info.setName(name);
        BmobIM.getInstance().startPrivateConversation(info,true,new ConversationListener() {
            @Override
            public void done(final BmobIMConversation c, BmobException e) {
                if(e==null){
                    mBmobIMConversation=BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
                    BmobIMTextMessage msg = new BmobIMTextMessage();
                    msg.setContent(message);
                    mBmobIMConversation.sendMessage(msg, new MessageSendListener() {
                        @Override
                        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                            if(e == null){
                                onclick(et_Message.getText().toString());
                                et_Message.setText("");
                            }else {
                                toast("发送失败");
                            }
                        }
                    });
                }else{
                    Log.d(TAG, "done: 开启会话出错");
                }
            }
        });
    }
    private void onclick(String message){
        Msg msg = new Msg();
        msg.setContent(message);
        msg.setType(Msg.TYPE_SENT);
        msgList.add(msg);
        //adapter.notifyDataSetChanged();
        adapter.notifyItemInserted(msgList.size() - 1);
        msg_recycle.scrollToPosition(msgList.size() - 1);
    }
}
