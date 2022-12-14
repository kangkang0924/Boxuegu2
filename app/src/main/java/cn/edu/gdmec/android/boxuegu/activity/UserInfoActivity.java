package cn.edu.gdmec.android.boxuegu.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.bean.UserBean;
import cn.edu.gdmec.android.boxuegu.utils.AnalysisUtils;
import cn.edu.gdmec.android.boxuegu.utils.DBUtils;

/**
 * Created by Jack on 2022/11/16.搞定
 * 用户信息修改
 */


public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_back;
    private ImageView iv_head_icon;
    private Bitmap head;
    private static String path = "/sdcard/myHead/";// sd路径
    private TextView tv_select_gallery;
    private TextView tv_select_camera;
    private TextView tv_main_title;
    private TextView tv_nickName,tv_signature,tv_user_name,tv_sex;
    private RelativeLayout rl_nickName,rl_sex,rl_signature,rl_title_bar,rl_head;
    //修改昵称的自定义常量
    private static final int CHANGE_NICKNAME = 4;
    //修改个性签名的自定义常量
    private static final int CHANGE_SIGNATURE = 5;
    private String spUserName;
    //private MyInfoView mMyInfoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //获取登录时的用户名
        spUserName = AnalysisUtils.readLoginUserName(this);
        init();
        initData();
        setListener();
    }

    /**
     * 初始化控件
     */
    private void init(){
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("个人资料");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        rl_nickName = (RelativeLayout) findViewById(R.id.rl_nickName);
        rl_sex = (RelativeLayout)findViewById(R.id.rl_sex);
        rl_signature = (RelativeLayout) findViewById(R.id.rl_signatrue);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_signature = (TextView) findViewById(R.id.tv_signatrue);

        iv_head_icon = (ImageView) findViewById(R.id.iv_head_icon);
        rl_head = (RelativeLayout) findViewById(R.id.rl_head);
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            iv_head_icon.setImageDrawable(drawable);
        } else {
            /**
             * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             *
             */
        }
    }

    /**
     * 获取数据
     */
    private void initData(){
        UserBean bean = null;
        bean = DBUtils.getInstance(this).getUserInfo(spUserName);
        //首先判断一下数据库是否有数据
        if (bean == null){
            bean = new UserBean();
            bean.userName = spUserName;
            bean.nickName = "问答精灵";
            bean.sex = "女";
            bean.signature = "问答精灵";
            //保存用户信息到数据库
            DBUtils.getInstance(this).saveUserInfo(bean);
        }
        setValue(bean);
    }

    /**
     * 为界面控件设置值
     */
    private void setValue(UserBean bean){
        tv_nickName.setText(bean.nickName);
        tv_user_name.setText(bean.userName);
        tv_sex.setText(bean.sex);
        tv_signature.setText(bean.signature);
    }

    /**
     * 设置控件的点击监听事件
     */
    private void setListener(){
        tv_back.setOnClickListener(this);
        rl_nickName.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_signature.setOnClickListener(this);
        rl_head.setOnClickListener(this);
    }


    /**
     * 控件的点击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_back:

                finish();
                break;

            case R.id.rl_head:
                showTypeDialog();
                break;
            case R.id.rl_nickName://昵称点击事件
                String name = tv_nickName.getText().toString();
                Bundle bdName = new Bundle();
                bdName.putString("content", name);
                bdName.putString("title", "昵称");
                bdName.putInt("flag", 1);
                enterActivityForResult(ChangeUserInfoActivity.class,CHANGE_NICKNAME,bdName);
                break;
            case R.id.rl_sex://性别点击事件
                String sex = tv_sex.getText().toString();
                sexDialong(sex);
                break;
            case R.id.rl_signatrue://签名的点击事件
                String signature = tv_signature.getText().toString();
                Bundle bdSignature = new Bundle();
                bdSignature.putString("content", signature);
                bdSignature.putString("title", "签名");
                bdSignature.putInt("flag", 2);
                enterActivityForResult(ChangeUserInfoActivity.class,CHANGE_SIGNATURE,bdSignature);
                break;
            default:
                break;
        }
    }

    /**
     * 设置性别的弹出框
     */
    private void sexDialong(String sex){
        int sexFlag = 0;
        if ("男".equals(sex)){
            sexFlag = 0;
        }else if ("女".equals(sex)){
            sexFlag = 1;
        }
        final String items[] = {"男","女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("性别");
        builder.setSingleChoiceItems(items, sexFlag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(UserInfoActivity.this,items[which],Toast.LENGTH_LONG).show();
                setSex(items[which]);
            }
        });
        builder.create().show();
    }

    /**
     * 设置头像的弹出框
     */
    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    /**
     * 更新数据库中的性别数据
     */
    private void setSex(String sex){
        tv_sex.setText(sex);
        //更新数据库中的性别字段
        DBUtils.getInstance(UserInfoActivity.this).updateUserInfo("sex",sex,spUserName);
    }

    /**
     * 回传数据
     */
    private String new_info;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        iv_head_icon.setImageBitmap(head);// 用ImageView显示出来
                    }
                }

            case CHANGE_NICKNAME:
                if (data != null){
                    new_info = data.getStringExtra("nickName");
                    if (TextUtils.isEmpty(new_info) || new_info == null){
                        return;
                    }
                    tv_nickName.setText(new_info);
                    //更新数据库中的昵称字段
                    DBUtils.getInstance(UserInfoActivity.this).updateUserInfo("nickName", new_info, spUserName);
                }
                break;
            case CHANGE_SIGNATURE:
                if (data != null){
                    new_info = data.getStringExtra("signature");
                    if (TextUtils.isEmpty(new_info) || new_info == null){
                        return;
                    }
                    tv_signature.setText(new_info);
                    //更新数据库中的昵称字段
                    DBUtils.getInstance(UserInfoActivity.this).updateUserInfo("signature", new_info, spUserName);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取回传数据时需使用的跳转方法
     * 第一个参数to表示需要跳转到的界面，第二个参数requestCode表示一个请求码，第三个参数b表示跳转时传递的数据
     */
    public void enterActivityForResult(Class<?> to, int requestCode,Bundle b){
        Intent i = new Intent(this,to);
        i.putExtras(b);
        startActivityForResult(i,requestCode);

    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 40);
        intent.putExtra("outputY", 40);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
