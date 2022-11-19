package cn.edu.gdmec.android.boxuegu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.utils.MD5Utils;

/**
 * Created by Jack on 2022/11/16.搞定
 * 登录Activity
 */

public class LoginActivity extends AppCompatActivity {

    private TextView tv_main_title;
    private TextView tv_back;
    private TextView tv_register;
    private TextView tv_find_psw;
    private Button btn_login;
    private EditText et_user_name;
    private EditText et_psw;
    private String username;
    private String psw;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    //获取页面控件
    private void init() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_find_psw = (TextView) findViewById(R.id.tv_find_psw);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_psw = (EditText) findViewById(R.id.et_psw);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
//        立即注册按钮的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //        立即找回密码的点击事件
        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到找回密码页面
                Intent intent = new Intent(LoginActivity.this, FindPswActivity.class);
                startActivity(intent);
            }
        });
        //登录按钮点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();
                String md5Psw = null;
                md5Psw = MD5Utils.md5(psw);
                String spPsw = readPsw(username);
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (md5Psw.equals(spPsw)) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //保存登录状态和登录用户名
                    saveLoginStatus(true, username);
                    Intent data = new Intent();
                    data.putExtra("isLogin", true);
                    setResult(RESULT_OK, data);
                    LoginActivity.this.finish();
                    //跳到主页
                } else if (!TextUtils.isEmpty(psw) && !md5Psw.equals(spPsw)) {
                    Toast.makeText(LoginActivity.this, "用户名和密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "此用户不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveLoginStatus(boolean status, String username) {
        //loginInfo表示文件名
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();//获取编辑器
        editor.putBoolean("isLogin", status);//存入boolean类型的登录状态
        editor.putString("loginUserName", username);//存入登录时的用户名
        editor.commit();//提交修改

    }

    private String readPsw(String username) {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getString(username, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            //从注册界面传递过来的用户名
            String userName = data.getStringExtra("userName");
            if (!TextUtils.isEmpty(userName)) {
                et_user_name.setText(userName);
                //设置光标位置
                et_user_name.setSelection(userName.length());
            }
        }
    }
}
