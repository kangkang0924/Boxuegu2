package cn.edu.gdmec.android.boxuegu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.gdmec.android.boxuegu.R;

/**
 * Created by Jack on 2022/11/16.搞定
 * 更改用户信息Activity
 */

public class ChangeUserInfoActivity extends AppCompatActivity {

    private String title;
    private String content;
    private int flag;
    private TextView tv_back;
    private TextView tv_main_title;
    private RelativeLayout rl_title_bar;
    private TextView tv_save;
    private ImageView iv_delete;
    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        init();
    }

    private void init() {
        //从个人资料页面传递过来的标题和内容
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        flag = getIntent().getIntExtra("flag",0);

        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText(title);
        rl_title_bar = (RelativeLayout)findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setVisibility(View.VISIBLE);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        et_content = (EditText) findViewById(R.id.et_content);
        if (!TextUtils.isEmpty(content)){
            et_content.setText(content);
            et_content.setSelection(content.length());
        }
        contentListener();
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeUserInfoActivity.this.finish();
            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_content.setText("");
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                String etContent = et_content.getText().toString().trim();
                switch (flag){
                    case 1:
                        if (!TextUtils.isEmpty(etContent)){
                            data.putExtra("nickName",etContent);
                            setResult(RESULT_OK,data);
                            Toast.makeText(ChangeUserInfoActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                            ChangeUserInfoActivity.this.finish();
                        }else {
                            Toast.makeText(ChangeUserInfoActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        if (!TextUtils.isEmpty(etContent)){
                            data.putExtra("signature",etContent);
                            setResult(RESULT_OK,data);
                            Toast.makeText(ChangeUserInfoActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                            ChangeUserInfoActivity.this.finish();
                        }else {
                            Toast.makeText(ChangeUserInfoActivity.this,"签名不能为空",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }
    //监听个人资料修改界面的输入文字
    private void contentListener() {
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = et_content.getText();
                int len = editable.length();//输入文本长度
                if (len>0){
                    iv_delete.setVisibility(View.VISIBLE);
                }else {
                    iv_delete.setVisibility(View.GONE);
                }
                switch (flag){
                    case 1://昵称
                        //昵称长度最多八个文字，超过八个需要截取处理
                        if (len>8){
                            int selEndIndex = Selection.getSelectionEnd(editable);
                            String str = editable.toString();
                            //截取字符串
                            String newstr = str.substring(0, 8);
                            et_content.setText(newstr);
                            editable = et_content.getText();
                            //新的字符串的长度
                            int newLen = editable.length();
                            //从旧光标位置超过新字符串的长度
                            if (selEndIndex > newLen){
                                selEndIndex = editable.length();
                            }
                            //设置新光标所在的位置
                            Selection.setSelection(editable,selEndIndex);
                        }
                        break;
                    case 2://签名
                        //签名长度最多十六个文字，超过十六个需要截取处理
                        if (len>16){
                            int selEndIndex = Selection.getSelectionEnd(editable);
                            String str = editable.toString();
                            //截取字符串
                            String newstr = str.substring(0, 16);
                            et_content.setText(newstr);
                            editable = et_content.getText();
                            //新的字符串的长度
                            int newLen = editable.length();
                            //从旧光标位置超过新字符串的长度
                            if (selEndIndex > newLen){
                                selEndIndex = editable.length();
                            }
                            //设置新光标所在的位置
                            Selection.setSelection(editable,selEndIndex);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}

