package cn.edu.gdmec.android.boxuegu.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.boxuegu.bean.CourseBean;
import cn.edu.gdmec.android.boxuegu.fragment.AdBannerFragment;
import cn.edu.gdmec.android.boxuegu.view.CourseView;

/**
 * Created by Jack on 2022/11/16.搞定
 */


public class AdBannerAdapter extends FragmentStatePagerAdapter implements View.OnTouchListener {

    private Handler mHanlder;
    private List<CourseBean> cadl;

    public AdBannerAdapter(FragmentManager fm) {
        super(fm);
        cadl = new ArrayList<>();
    }

    public AdBannerAdapter(FragmentManager fm, Handler handler) {
        super(fm);
        cadl = new ArrayList<>();
        mHanlder = handler;
    }

    //设置数据更新界面
    public void setDatas(List<CourseBean> cadl) {
        this.cadl = cadl;
        //更新操作
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        //防止刷新结果显示列表时出现缓存数据，重载此方法，使之默认返回POSITION_NONE
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        if (cadl.size() > 0) {
            args.putString("ad", cadl.get(position % cadl.size()).icon);
        }
        return AdBannerFragment.newInstance(args);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public int getSize() {
        return cadl == null ? 0 : cadl.size();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mHanlder.removeMessages(CourseView.MSG_AD_SLID);
        return false;
    }
}

