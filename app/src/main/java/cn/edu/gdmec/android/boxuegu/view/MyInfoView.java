package cn.edu.gdmec.android.boxuegu.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.activity.LoginActivity;
import cn.edu.gdmec.android.boxuegu.activity.PlayHistoryActivity;
import cn.edu.gdmec.android.boxuegu.activity.SettingActivity;
import cn.edu.gdmec.android.boxuegu.activity.UserInfoActivity;
import cn.edu.gdmec.android.boxuegu.utils.AnalysisUtils;

/**
 * Created by Jack on 2018/3/7.
 */


public class MyInfoView {
    private ImageView iv_head_icon;
    private LinearLayout ll_head;
    private RelativeLayout rl_course_history,rl_setting;
    private TextView tv_user_name;
    private Activity mContext;
    private final LayoutInflater mInflater;
    private View mCurrentView;

    private  String path = "/sdcard/myHead/head.jpg";


    public MyInfoView(Activity context){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    private void createView(){
        initView();
    }

    /**
     * 获取界面控件
     */
    private void initView(){
        mCurrentView = mInflater.inflate(R.layout.main_view_myinfo,null);
        ll_head = (LinearLayout) mCurrentView.findViewById(R.id.ll_head);
        iv_head_icon = (ImageView) mCurrentView.findViewById(R.id.iv_head_icon);
        rl_course_history = (RelativeLayout) mCurrentView.findViewById(R.id.rl_course_history);
        rl_setting = (RelativeLayout) mCurrentView.findViewById(R.id.rl_setting);
        tv_user_name = (TextView) mCurrentView.findViewById(R.id.tv_user_name);
        mCurrentView.setVisibility(View.VISIBLE);
        setLoginParams(readLoginStatus());
        ll_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否已经登录
                if (readLoginStatus()){
                    //已登录跳转到个人资料界面
                    Intent intent = new Intent(mContext,UserInfoActivity.class);
                    mContext.startActivity(intent);

                }else{
                    //未登录跳转到登录界面
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivityForResult(intent,1);
                }
            }
        });

        rl_course_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readLoginStatus()){
                    //跳转到播放记录界面
                    Intent intent = new Intent(mContext,PlayHistoryActivity.class);
                    mContext.startActivity(intent);
                }else {
                    Toast.makeText(mContext,"您还未登录，请先登录",Toast.LENGTH_LONG).show();
                }
            }
        });

        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readLoginStatus()){
                    //跳转到设置界面
                    Intent intent = new Intent(mContext,SettingActivity.class);
                    mContext.startActivityForResult(intent,1);
                }else {
                    Toast.makeText(mContext,"您还未登录，请先登录",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * 登录成功后设置我的界面
     * @return
     */
    public void setLoginParams(boolean isLogin){
        if (isLogin){
            tv_user_name.setText(AnalysisUtils.readLoginUserName(mContext));
            File file = new File(path);
            if (file.exists()){
                Bitmap bm = BitmapFactory.decodeFile(path);
                iv_head_icon.setImageBitmap(bm);
            }
        }else {
            tv_user_name.setText("点击登录");
        }
    }

    /**
     * 获取当前在导航栏上方显示对应的View
     * @return
     */
    public View getView(){
        if (mCurrentView == null){
            createView();
        }
        return mCurrentView;
    }

    /**
     *显示当前在导航栏上方显示对应的View
     * @return
     */
    public void showView(){
        if (mCurrentView == null){
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }

    /**
     * 读取登录状态
     * @return
     */
    private boolean readLoginStatus(){
        SharedPreferences sp = mContext.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin",false);
        return isLogin;
    }
}
