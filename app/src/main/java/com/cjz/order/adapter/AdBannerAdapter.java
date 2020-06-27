package com.cjz.order.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cjz.order.activity.ShopActivity;
import com.cjz.order.bean.AdBean;
import com.cjz.order.fragment.AdBannerFragment;

import java.util.ArrayList;
import java.util.List;

public class AdBannerAdapter extends FragmentStatePagerAdapter implements OnTouchListener {
    private Handler mHandler;
    private List<AdBean> cadl;
    public AdBannerAdapter(FragmentManager fm){
        super(fm);
        cadl =new ArrayList<AdBean>();

    }
    public AdBannerAdapter(FragmentManager fm, Handler handler){
        super(fm);
        mHandler= handler;
        cadl =new ArrayList<AdBean>();

    }
    /**
     *设置数据更新界面
     */


    public void setDatas(List<AdBean> cadl){
        this.cadl= cadl;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int index) {
        Bundle args = new Bundle();

        if (cadl.size()>0){
            args.putString("ad",cadl.get(index % cadl.size()).icon);
        }
        return AdBannerFragment.newInstance(args);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    /**
     *返回数据集的真实容量大小
     */
    public  int getSize(){
        return cadl==null ? 0  : cadl.size();
     }
     @Override
     public int getItemPosition(Object object){
        //防止刷新结果显示列表时出现缓存数据，重载这个函数，使用默认返回POSITION_NONE
         return  POSITION_NONE;
     }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mHandler.removeMessages(ShopActivity.MSG_AD_SLID);
        return false;
    }
}
