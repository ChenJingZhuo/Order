package com.cjz.order.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.cjz.order.R;

public class AdBannerFragment extends Fragment {
    private String ab; //广告ID
    private ImageView iv;  // 图片

    public static AdBannerFragment newInstance(Bundle args) {
        AdBannerFragment af = new AdBannerFragment();
        af.setArguments(args);
        return af;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        Bundle arg = getArguments();
        //获取广告图片名称
        ab = arg.getString("ad");
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
    }

    @Override
    public void onResume() {

        super.onResume();
        if (ab != null) {
            if ("banner".equals(ab)) {
                iv.setImageResource(R.drawable.banner);
            } else if ("hghg".equals(ab)) {
                iv.setImageResource(R.drawable.hghg);
            } else if ("jnp".equals(ab)) {
                iv.setImageResource(R.drawable.jnp);
            }
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (iv != null) {
            iv.setImageDrawable(null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        //创建广告图片的控件
        iv = new ImageView(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(lp);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        return iv;
    }
}
