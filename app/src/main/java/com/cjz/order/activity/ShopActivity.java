package com.cjz.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.cjz.order.R;
import com.cjz.order.adapter.AdBannerAdapter;
import com.cjz.order.adapter.ShopAdapter;
import com.cjz.order.bean.AdBean;
import com.cjz.order.bean.ShopBean;
import com.cjz.order.searchmlist.SearchActivity;
import com.cjz.order.utils.Constant;
import com.cjz.order.utils.JsonParse;
import com.cjz.order.views.ShopListView;
import com.cjz.order.views.ViewPagerIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShopActivity extends AppCompatActivity {

    private ViewPager adPager;    //广告
    private View adBannerLay;    //广告条容器
    private AdBannerAdapter ada;   //适配器
    public static final int MSG_AD_SLID = 002;   //广告自动滑动
    private ViewPagerIndicator vpi;//小圆点
    private MHandler2 mHandler2;  //事件捕获
    private List<AdBean> cadl;

    private TextView tv_back, tv_title;         //返回键与标题控件
    private ShopListView slv_list;              //列表控件
    private ShopAdapter adapter;                //列表的适配器
    public static final int MSG_SHOP_OK = 1; //获取数据
    private MHandler mHandler;
    private RelativeLayout rl_title_bar;
    private ImageView mIvSearch;

    public static List<String> history_search; //历史记录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        mHandler = new MHandler();
        initData();
        mHandler2 = new MHandler2();
        initAdData();
        init();
        new AdAutoSlidThread().start();
        mIvSearch = (ImageView) findViewById(R.id.iv_search);
        mIvSearch.setVisibility(View.VISIBLE);
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setData
                Intent intent = new Intent(ShopActivity.this, SearchActivity.class);
                intent.putExtra("shop_food",pythonList);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化界面控件
     */
    private void init() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("店铺");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tv_back.setVisibility(View.GONE);
        slv_list = (ShopListView) findViewById(R.id.slv_list);
        adapter = new ShopAdapter(this);
        slv_list.setAdapter(adapter);

        adPager = (ViewPager) findViewById(R.id.vp_advertBanner);
        adPager.setLongClickable(false);
        ada = new AdBannerAdapter(getSupportFragmentManager(), mHandler2);
        adPager.setAdapter(ada);     //给ViewPager设置适配器
        adPager.setOnTouchListener(ada);
        vpi = (ViewPagerIndicator) findViewById(R.id.vpi_advert_indicator);
        vpi.setCount(ada.getSize());   //设置小圆点的个数
        adBannerLay = findViewById(R.id.r1_adBanner);
        adPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (ada.getSize() > 0) {
                    //由于index数据在滑动时是累加的，
                    //因此用index % ada.getSize()来标记滑动到的当前位置
                    vpi.setCurrentPosition(position % ada.getSize());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        resetSize();
        if (cadl != null) {
            if (cadl.size() > 0) {
                vpi.setCount(cadl.size());
                vpi.setCurrentPosition(0);
            }
            ada.setDatas(cadl);
        }

    }

    private void initData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Constant.WEB_SITE + Constant.REQUEST_SHOP_URL).build();
        Call call = okHttpClient.newCall(request);
        // 开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string(); //获取店铺数据
                Log.d("ShopActivity", res);
                Message msg = new Message();
                msg.what = MSG_SHOP_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 事件捕获
     */
    private ArrayList<ShopBean> pythonList;
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_SHOP_OK:
                    if (msg.obj != null) {
                        String vlResult = (String) msg.obj;
                        //解析获取的JSON数据
                        pythonList = (ArrayList<ShopBean>) JsonParse.getInstance().
                                getShopList(vlResult);
                        adapter.setData(pythonList);
                    }
                    break;
            }
        }
    }

    protected long exitTime;//记录第一次点击时的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(ShopActivity.this, "再按一次退出订餐应用",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ShopActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 事件捕获
     */
    class MHandler2 extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_AD_SLID:
                    if (ada.getCount() > 0) {
                        adPager.setCurrentItem(adPager.getCurrentItem() + 1);
                    }
                    break;
            }
        }
    }

    /**
     * 广告自动滑动
     */
    class AdAutoSlidThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mHandler2 != null) {
                    mHandler2.sendEmptyMessage(MSG_AD_SLID);
                }
            }
        }
    }

    /**
     * 计算控件大小
     */
    private void resetSize() {
        int sw = getScreenWidth(this);
        int adLheight = sw / 2; //广告条高度
        ViewGroup.LayoutParams adlp = adBannerLay.getLayoutParams();
        adlp.width = sw;
        adlp.height = adLheight;
        adBannerLay.setLayoutParams(adlp);
    }

    /**
     * 读取屏幕宽度
     */
    private static int getScreenWidth(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = context.getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 初始化广告中的数据
     */
    private void initAdData() {
        cadl = new ArrayList<AdBean>();
        for (int i = 0; i < 3; i++) {
            AdBean bean = new AdBean();
            bean.id = (i + 1);
            switch (i) {
                case 0:
                    bean.icon = "banner";
                    break;
                case 1:
                    bean.icon = "hghg";
                    break;
                case 2:
                    bean.icon = "jnp";
                    break;
            }
            cadl.add(bean);
        }
    }
}
