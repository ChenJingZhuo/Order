package com.cjz.order.searchmlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cjz.order.R;
import com.cjz.order.activity.SearchResultActivity;
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
        String shareData = "澳洲美食,长沙美食,韩国料理,日本料理,舌尖上的中国,意大利餐,山西菜";
        List<String> skills = Arrays.asList(shareData.split(","));

        String shareHotData ="粤菜,浙菜,苏菜";
        List<String> skillHots = Arrays.asList(shareHotData.split(","));

        msearchLy.initData(skills, skillHots, new SearchLayout.setSearchCallBackListener() {
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
            }
            @Override
            public void SaveOldData(ArrayList<String> AlloldDataList) {
                //保存所有的搜索记录
            }
        });
    }

}
