package com.cjz.order.searchmlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cjz.order.R;
import com.cjz.order.activity.SearchResultActivity;
import com.cjz.order.activity.ShopActivity;
import com.cjz.order.bean.FoodBean;
import com.cjz.order.bean.ShopBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchLayout msearchLy;
    private ArrayList<ShopBean> shopBeans;
    private ArrayList<ShopBean> shopBeans2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        msearchLy = (SearchLayout)findViewById(R.id.msearchlayout);
        initData();
        shopBeans = (ArrayList<ShopBean>) getIntent().getSerializableExtra("shop_food");
    }

    protected void initData() {
        if (ShopActivity.history_search == null || ShopActivity.history_search.size() == 0){
            ShopActivity.history_search = new ArrayList<>();
            ShopActivity.history_search.add("澳洲美食");
            ShopActivity.history_search.add("长沙美食");
            ShopActivity.history_search.add("韩国料理");
            ShopActivity.history_search.add("日本料理");
            ShopActivity.history_search.add("舌尖上的中国");
            ShopActivity.history_search.add("意大利餐");
            ShopActivity.history_search.add("山西菜");
        }

        String shareHotData ="粤菜,浙菜,苏菜";
        List<String> skillHots = Arrays.asList(shareHotData.split(","));

        msearchLy.initData(ShopActivity.history_search, skillHots, new SearchLayout.setSearchCallBackListener() {
            @Override
            public void Search(String str) {
                shopBeans2 = new ArrayList<>();
                for (ShopBean shopBean : shopBeans) {
                    if (shopBean.getShopName().contains(str)){
                        shopBeans2.add(shopBean);
                        continue;
                    }

                    for (FoodBean foodBean : shopBean.getFoodList()) {
                        if (foodBean.getFoodName().contains(str)){
                            shopBeans2.add(shopBean);
                            break;
                        }
                    }

                }

                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("search_result",shopBeans2);
                startActivity(intent);

                //进行或联网搜索
            }
            @Override
            public void Back() {
                finish();
            }

            @Override
            public void ClearOldData() {
                //清除历史搜索记录  更新记录原始数据
                ShopActivity.history_search.clear();
            }
            @Override
            public void SaveOldData(ArrayList<String> AlloldDataList) {
                //保存所有的搜索记录
                ShopActivity.history_search = AlloldDataList;
            }
        });
    }

}
