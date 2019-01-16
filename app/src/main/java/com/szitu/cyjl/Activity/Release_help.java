package com.szitu.cyjl.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.szitu.cyjl.Adapter.Recycle_Adapter;
import com.szitu.cyjl.JavaBean.recycle_bean;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.Subscribe;
import com.szitu.cyjl.bmob.User;
import com.szitu.cyjl.util.FileChooseUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import static com.szitu.cyjl.Activity.PhotoQuery.TAKE_PHOTO;

public class Release_help extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Release_help";
    private ImageView icon1;
    private Uri imageUri;
    private File outputImage;
    private Bitmap bitmap;
    private PopupWindow popupWindow;
    private View contentView;
    private Button ok,delect,retu;
    private Toolbar toolbar;
    private TextView tb_text,tb_local;
    private EditText local,price_min,price_max,introduce,number,ry_name;
    private Button bt_subscribe;
    private SharedPreferences sharedPreferences;
    private String mylocal;
    private ArrayList<String> filebyte;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private ArrayList<recycle_bean> list;
    private Recycle_Adapter recyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_release_help);
        initView();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.fanhui);
        }
        showPopwindow();
    }

    private void initView(){
        icon1 = (ImageView)findViewById(R.id.subscribe_icon1);
        icon1.setOnClickListener(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tb_text = (TextView)findViewById(R.id.tb_text);
        tb_text.setText("人员互助");
        tb_local = (TextView)findViewById(R.id.tb_local);
        tb_local.setVisibility(View.GONE);
        list = new ArrayList<>();
        recyAdapter = new Recycle_Adapter(list);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recyAdapter);
        bt_subscribe = (Button)findViewById(R.id.bt_subscribe);
        bt_subscribe.setOnClickListener(this);
        number = (EditText)findViewById(R.id.et_manNumber);
        ry_name = (EditText)findViewById(R.id.ry_name);
        local = (EditText)findViewById(R.id.et_local);
        price_min = (EditText)findViewById(R.id.et_price_min);
        price_max = (EditText)findViewById(R.id.et_price_max);
        introduce = (EditText)findViewById(R.id.et_introduce);
        sharedPreferences = getSharedPreferences("local",MODE_PRIVATE);
        mylocal = sharedPreferences.getString("province","江苏省");
        mylocal+=sharedPreferences.getString("city","苏州市");
        mylocal+=sharedPreferences.getString("district","吴江区");
        local.setText(mylocal);
        filebyte = new ArrayList<>();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.subscribe_icon1:
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.bt_subscribe:
                submit();
                break;
            case R.id.button_ok:
                photograph();
                if(popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            case R.id.button_delete:
                photo();
                if(popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            case R.id.button_return:
                if(popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
        }
    }
    private void photograph(){
        outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(Release_help.this, "com.example.cameraalbumtest.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
    private void photo(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");//选择图片
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择上传的文件"), 67);
        }catch (ActivityNotFoundException ex){
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        String path = outputImage.getAbsolutePath();
                        filebyte.add(path);
                        recycle_bean bean = new recycle_bean();
                        bean.setPath(path);
                        list.add(bean);
                        recyAdapter.notifyDataSetChanged();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 67:
                try {
                    //该uri是上一个Activity返回的
                    imageUri = data.getData();

                    if(imageUri!=null) {
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        String path = FileChooseUtil.getInstance(this).getChooseFileResultPath(data.getData());
                        filebyte.add(path);
                        recycle_bean bean = new recycle_bean();
                        bean.setPath(path);
                        list.add(bean);
                        recyAdapter.notifyDataSetChanged();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    private void showPopwindow() {
        //加载弹出框的布局
        contentView = LayoutInflater.from(Release_help.this).inflate(
                R.layout.pop, null);
        ok = (Button)contentView.findViewById(R.id.button_ok);
        ok.setOnClickListener(this);
        delect = (Button)contentView.findViewById(R.id.button_delete);
        delect.setOnClickListener(this);
        retu = (Button)contentView.findViewById(R.id.button_return);
        retu.setOnClickListener(this);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画，指定刚才定义的style
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        // 按下android回退物理键 PopipWindow消失解决

    }
    private void submit(){
        String locl = local.getText().toString();
        String min = price_min.getText().toString();
        String max = price_max.getText().toString();
        String content = introduce.getText().toString();
        if(content == null || content.equals("")){
            Toast.makeText(getApplicationContext(),"请填写介绍",Toast.LENGTH_LONG).show();
            return;
        }
        dialog();
        insertBatchDatasWithOne(filebyte);
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
    private void dialog(){
        progressDialog=new ProgressDialog(Release_help.this);
        progressDialog.setMessage("正在上传...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public void insertBatchDatasWithOne(ArrayList<String> listpath){
        final String []lists = listpath.toArray(new String[listpath.size()]);
        BmobFile.uploadBatch(lists, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if(urls.size()==lists.length){//如果数量相等，则代表文件全部上传完成
                    //do something
                    for(String s : urls){
                        Log.d(TAG, "onSuccess: "+s);
                    }
                    saveFile(ry_name.getText().toString(),number.getText().toString(),"人员互助",local.getText().toString(),price_min.getText().toString(),price_max.getText().toString(),introduce.getText().toString(),urls);
                }
            }
            @Override
            public void onError(int statuscode, String errormsg) {
                ShowToast("错误码"+statuscode +",错误描述："+errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
    }
    private void saveFile(String name,String number,String leixing,String locl,String min,String max,String content,List<String> filebytes){
        com.szitu.cyjl.bmob.Subscribe subscribe = new Subscribe();
        subscribe.setUsername(User.getUserInfo().getNickname());
        subscribe.setUserOblectID(User.getUserInfo().getObjectId());
        subscribe.setUerIcon(User.getUserInfo().getUserlcon());
        subscribe.setType(leixing);
        subscribe.setLocal(locl);
        subscribe.setMin(min);
        subscribe.setMax(max);
        subscribe.setIntroduce(content);
        subscribe.setFilebyte(filebytes);
        subscribe.setSp_name(name);
        subscribe.setSp_number(number);
        subscribe.setPhone(User.getUserInfo().getMobilePhoneNumber());
        subscribe.setLocaltime(new Date().toLocaleString());
        subscribe.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(Release_help.this,MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"发布失败,请检查网络",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });
    }
    private void ShowToast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
    }
}
