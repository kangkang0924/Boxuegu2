package cn.edu.gdmec.android.boxuegu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.activity.LoginActivity;
import cn.edu.gdmec.android.boxuegu.activity.VideoListActivity;
import cn.edu.gdmec.android.boxuegu.bean.CourseBean;

/**
 * Created by Jack on 2022/11/16.搞定
 */


public class CourseAdapter extends BaseAdapter {
    private Activity context;
    private List<List<CourseBean>> cbl;

    public CourseAdapter(Activity context) {
        this.context = context;
    }
    //设置数据更新界面
    public void setData(List<List<CourseBean>> cbl) {
        this.cbl = cbl;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cbl == null ? 0 : cbl.size();
    }

    @Override
    public Object getItem(int position) {
        return cbl == null ? 0 : cbl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.course_list_item,null);
            vh.iv_left_img = convertView.findViewById(R.id.iv_left_img);
            vh.iv_right_img = convertView.findViewById(R.id.iv_right_img);

            vh.tv_left_img_title = convertView.findViewById(R.id.tv_left_img_title);
            vh.tv_left_title = convertView.findViewById(R.id.tv_left_title);

            vh.tv_right_img_title = convertView.findViewById(R.id.tv_right_img_title);
            vh.tv_right_title = convertView.findViewById(R.id.tv_right_title);
            convertView.setTag(vh);

        }else {
            vh = (ViewHolder) convertView.getTag();
        }

        //开始绑定item数据
        List<CourseBean> list = (List<CourseBean>) getItem(position);
        if (list != null){
            for (int i=0;i<list.size();i++){
                final CourseBean bean = list.get(i);
                switch (i){
                    case 0:
                        //左边
                        vh.tv_left_img_title.setText(bean.imgTitle);
                        vh.tv_left_title.setText(bean.title);
                        setLeftImg(bean.id,vh.iv_left_img);
                        vh.iv_left_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (readLoginStatus()) {
                                    //跳转到课程详情界面
                                    Intent intent = new Intent(context, VideoListActivity.class);
                                    intent.putExtra("id", bean.id);
                                    intent.putExtra("intro", bean.intro);
                                    context.startActivity(intent);
                                }else {
                                    //添加提示
                                    Toast.makeText(context,"您还未登录，请先登录",Toast.LENGTH_SHORT).show();
                                    //未登录跳转到登录界面
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    //带回用户名
                                    context.startActivityForResult(intent,1);
                                }
                            }
                        });
                        break;
                    case 1:
                        //右边
                        vh.tv_right_img_title.setText(bean.imgTitle);
                        vh.tv_right_title.setText(bean.title);
                        setRightImg(bean.id,vh.iv_right_img);
                        vh.iv_right_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (readLoginStatus()) {
                                    //跳转到课程详情界面
                                    Intent intent = new Intent(context, VideoListActivity.class);
                                    intent.putExtra("id", bean.id);
                                    intent.putExtra("intro", bean.intro);
                                    context.startActivity(intent);
                                }else {
                                    Toast.makeText(context,"您还未登录，请先登录",Toast.LENGTH_SHORT).show();
                                    //未登录跳转到登录界面
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    //带回用户名
                                    context.startActivityForResult(intent,1);
                                }
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        }

        return convertView;
    }

    private void setRightImg(int id, ImageView iv_right_img) {
        switch (id){
            case 2:
                iv_right_img.setImageResource(R.drawable.chapter_2_icon);
                break;
            case 4:
                iv_right_img.setImageResource(R.drawable.chapter_4_icon);
                break;
            case 6:
                iv_right_img.setImageResource(R.drawable.chapter_6_icon);
                break;
            case 8:
                iv_right_img.setImageResource(R.drawable.chapter_8_icon);
                break;
            case 10:
                iv_right_img.setImageResource(R.drawable.chapter_10_icon);
                break;
        }
    }

    private void setLeftImg(int id, ImageView iv_left_img) {
        switch (id){
            case 1:
                iv_left_img.setImageResource(R.drawable.chapter_1_icon);
                break;
            case 3:
                iv_left_img.setImageResource(R.drawable.chapter_3_icon);
                break;
            case 5:
                iv_left_img.setImageResource(R.drawable.chapter_5_icon);
                break;
            case 7:
                iv_left_img.setImageResource(R.drawable.chapter_7_icon);
                break;
            case 9:
                iv_left_img.setImageResource(R.drawable.chapter_9_icon);
                break;
        }
    }

    class ViewHolder{
        public TextView tv_left_img_title,tv_left_title,tv_right_img_title,tv_right_title;
        public ImageView iv_left_img,iv_right_img;
    }
    private boolean readLoginStatus(){
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin",false);
        return isLogin;
    }
}
