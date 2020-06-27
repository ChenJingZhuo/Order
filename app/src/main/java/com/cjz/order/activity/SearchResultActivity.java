package com.cjz.order.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cjz.order.R;
import com.cjz.order.adapter.ShopAdapter;
import com.cjz.order.bean.ShopBean;
import com.cjz.order.views.ShopListView;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    private ArrayList<ShopBean> search_result;
    private TextView mTvTip;
    private ShopListView mSlvList;
    private TextView mTvBack;
    private TextView mTvTitle;
    private RelativeLayout mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mTitleBar = (RelativeLayout) findViewById(R.id.title_bar);
        mTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResultActivity.this.finish();
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("搜索结果");
        mTvTip = findViewById(R.id.tv_tip);
        mSlvList = findViewById(R.id.slv_list);
        search_result = (ArrayList<ShopBean>) getIntent().getSerializableExtra("search_result");
        if (search_result != null && search_result.size() > 0) {
            mTvTip.setText("共找到：" + search_result.size() + " 家相关店铺");
            ShopAdapter shopAdapter = new ShopAdapter(this);
            shopAdapter.setData(search_result);
            mSlvList.setAdapter(shopAdapter);
        }
    }
}
