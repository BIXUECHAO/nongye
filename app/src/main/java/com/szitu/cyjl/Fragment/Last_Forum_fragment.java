package com.szitu.cyjl.Fragment;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.szitu.cyjl.Activity.Logn;
import com.szitu.cyjl.Activity.My;
import com.szitu.cyjl.Activity.Release;
import com.szitu.cyjl.Activity.Release_help;
import com.szitu.cyjl.Activity.Release_shangpin;
import com.szitu.cyjl.Activity.Rrelease_Subscribe;
import com.szitu.cyjl.R;
import com.szitu.cyjl.bmob.User;
import com.szitu.cyjl.util.FileChooseUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.szitu.cyjl.Activity.PhotoQuery.TAKE_PHOTO;

public class Last_Forum_fragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "Last_Forum_fragment";
    private TextView nongji,help,shangpin,fabu,yuyue,qingqui,phoneText;
    private TextView username;
    private CircleImageView imageView;
    private PopupWindow popupWindow;
    private View contentView;
    private Button ok,delect,retu;
    private Uri imageUri;
    private File outputImage;
    private Bitmap bitmap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_last_forum,container,false);
        Bmob.initialize(getActivity(), "900b2dcf667a67b3403f4953c227c0cb");
        initView(view);
        return view;
    }
    private void initView(View view){
        nongji = (TextView)view.findViewById(R.id.my_nongji);
        nongji.setOnClickListener(this);
        help = (TextView)view.findViewById(R.id.my_help);
        help.setOnClickListener(this);
        shangpin = (TextView)view.findViewById(R.id.my_shangpin);
        shangpin.setOnClickListener(this);
        phoneText = (TextView)view.findViewById(R.id.phoneText);
        phoneText.setText(User.getUserInfo().getMobilePhoneNumber());
        fabu = (TextView)view.findViewById(R.id.my_fabu);
        fabu.setOnClickListener(this);
        yuyue = (TextView)view.findViewById(R.id.my_yuyue);
        yuyue.setOnClickListener(this);
        qingqui = (TextView)view.findViewById(R.id.my_qingqui);
        qingqui.setOnClickListener(this);
        username = (TextView)view.findViewById(R.id.my_logining);
        username.setOnClickListener(this);
        username.setText(User.getUserInfo().getNickname());
        imageView = (CircleImageView) view.findViewById(R.id.my_locn);
        Log.d(TAG, "initView: 图标"+User.getUserInfo().getUserlcon());
        imageView.setOnClickListener(this);
        showPopwindow();
        Glide.with(getActivity()).load(User.getUserInfo().getUserlcon()).placeholder(R.drawable.headphoto).into(imageView);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(!isUser()){
            toast("请先登录");
            return;
        }
        switch (v.getId()){
            case R.id.my_nongji:
                    Intent intent =new Intent(getActivity(),Rrelease_Subscribe.class);
                    alertRelease(intent);
                break;
            case R.id.my_help:
                    Intent intent1 = new Intent(getActivity(),Release_help.class);
                    alertRelease(intent1);
                break;
            case R.id.my_shangpin:
                    Intent intent2 = new Intent(getActivity(),Release_shangpin.class);
                    alertRelease(intent2);
                break;
            case R.id.my_fabu:
                Intent intent3 = new Intent(getActivity(),Release.class);
                startActivity(intent3);
                break;
            case R.id.my_yuyue:
                toast("预约");
                break;
            case R.id.my_qingqui:
                toast("请求");
                break;
            case R.id.my_locn:
                alert();
                break;
            case R.id.my_logining:
                if(User.getUserInfo().getObjectId() != null){
                    startActivity(new Intent(getActivity(),My.class));
                }else {
                    startActivity(new Intent(getActivity(),Logn.class));
                }
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
                default:break;
        }
    }
    private void toast(String content){
        Toast.makeText(getActivity(),content,Toast.LENGTH_SHORT).show();
    }
    private boolean isUser(){
        if(User.getUserInfo().getObjectId() != null){
            return true;
        }else {
            return false;
        }
    }
    private void showPopwindow() {
        //加载弹出框的布局
        contentView = LayoutInflater.from(getActivity()).inflate(
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
    private void photograph(){
        outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(getActivity(), "com.example.cameraalbumtest.fileprovider", outputImage);
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
            Toast.makeText(getActivity(), "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        String path = outputImage.getAbsolutePath();
                        updataicon(path);
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
                        Bitmap bit = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        String path = FileChooseUtil.getInstance(getActivity()).getChooseFileResultPath(data.getData());
                        updataicon(path);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    private void updataicon(String path){
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                   String url = bmobFile.getFileUrl();
                   updata(url);
                }else{
                    toast("上传文件失败：" + e.getMessage());
                }
            }
            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }
    private void updata(final String url){
        Log.d(TAG, "updata: "+url);
        User user = new User();
        user.setUserlcon(url);
        user.update(User.getUserInfo().getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    toast("上传成功");
                    Glide.with(getActivity()).load(url).placeholder(R.drawable.headphoto).into(imageView);
                }else {
                    toast("上传失败"+e.getMessage());
                }
            }
        });
    }
    private void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("是否更换图标")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
                    }
                })
                .setPositiveButton("取消",null);
        builder.show();
    }
    private void alertRelease(final Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("是否使用本电话")
                .setMessage(User.getUserInfo().getMobilePhoneNumber())
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(intent);
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toast("请在用户管理中修改电话");
                    }
                });
        builder.show();
    }
}
