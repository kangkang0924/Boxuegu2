package cn.edu.gdmec.android.boxuegu.view;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.adapter.AdBannerAdapter;
import cn.edu.gdmec.android.boxuegu.adapter.CourseAdapter;
import cn.edu.gdmec.android.boxuegu.bean.CourseBean;
import cn.edu.gdmec.android.boxuegu.utils.AnalysisUtils;

/**
 * Created by Jack on 2022/11/16.搞定
 */


public class CourseView {
    private ListView lv_list;
    private CourseAdapter adapter;
    private List<List<CourseBean>> cbl;
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;
    private ViewPager adPager;
    private View adBannerLay;
    private AdBannerAdapter ada;
    public static final int MSG_AD_SLID=002;
    private ViewPagerIndicator vpi;
    private MHandler mHandler;
    private List<CourseBean> cadl;
    public CourseView(FragmentActivity context){
        mContext = context;
        //为之后将layout转化为view时用
        mInflater = LayoutInflater.from(mContext);
    }
    private void createView() {
        mHandler = new MHandler();
        initAdData();
        getCourseData();
        initView();
        new AdAutoSlidThread().start();
    }
    //事件捕获
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case MSG_AD_SLID:
                    if (ada.getCount()>0){
                        adPager.setCurrentItem(adPager.getCurrentItem() + 1);
                    }
                    break;
            }
        }
    }
    //广告自动滑动
    class AdAutoSlidThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (true){
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mHandler != null)
                    mHandler.sendEmptyMessage(MSG_AD_SLID);
            }
        }
    }
    //初始化控件
    private void initView(){
        mCurrentView = mInflater.inflate(R.layout.main_view_course,null);
        lv_list = (ListView)mCurrentView.findViewById(R.id.lv_list);
        adapter = new CourseAdapter(mContext);
        adapter.setData(cbl);
        lv_list.setAdapter(adapter);
        adPager = (ViewPager)mCurrentView.findViewById(R.id.vp_advertBanner);
        adPager.setLongClickable(false);
        ada = new AdBannerAdapter(mContext.getSupportFragmentManager(),mHandler);
        adPager.setAdapter(ada);//给viewpager设置适配器
        adPager.setOnTouchListener(ada);
        vpi = (ViewPagerIndicator) mCurrentView.findViewById(R.id.vpi_advert_indict);
        vpi.setCount(ada.getSize());
        adBannerLay = mCurrentView.findViewById(R.id.rl_adBanner);
        adPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener( ) {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (ada.getSize()>0){
                    vpi.setCurrentPosition(position%ada.getSize());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        resetSize();
        if (cadl != null){
            vpi.setCount(cadl.size());
            vpi.setCurrentPosition(0);
        }
        ada.setDatas(cadl);

    }
    private void resetSize() {
        int sw = getScreenWith(mContext);
        int adLheight = sw / 2;
        ViewGroup.LayoutParams adlp = adBannerLay.getLayoutParams();
        adlp.width = sw;
        adlp.height = adLheight;
        adBannerLay.setLayoutParams(adlp);
    }
    public static int getScreenWith(Activity context){
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = context.getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }
    //初始化广告中的数据
    private void initAdData() {
        cadl = new ArrayList<CourseBean>();
        for (int i = 0;i<3;i++){
            CourseBean bean = new CourseBean();
            bean.id=(i+1);
            switch (i){
                case 0:
                    bean.icon="banner_1";
                    break;
                case 1:
                    bean.icon="banner_2";
                    break;
                case 2:
                    bean.icon="banner_3";
                    break;
                default:
                    break;
            }
            cadl.add(bean);
        }
    }
    //获取课程信息
    private void getCourseData(){
        try {
            InputStream is = mContext.getResources().getAssets().open("chaptertitle.xml");
            cbl = AnalysisUtils.getCourseInfos(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public View getView(){
        if (mCurrentView==null){
            createView();
        }return mCurrentView;
    }
    public void showView(){
        if (mCurrentView==null){
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }
}
