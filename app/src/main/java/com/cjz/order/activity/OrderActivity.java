package com.cjz.order.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cjz.order.R;
import com.cjz.order.adapter.OrderAdapter;
import com.cjz.order.bean.FoodBean;
import com.cjz.order.utils.MyOk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderActivity extends AppCompatActivity {
    private ListView lv_order;
    private OrderAdapter adapter;
    private List<FoodBean> carFoodList;
    private TextView tv_title, tv_back,tv_distribution_cost,tv_total_cost,
            tv_cost,tv_payment;
    private RelativeLayout rl_title_bar;
    private BigDecimal money,distributionCost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        //获取购物车中的数据
        carFoodList= (List<FoodBean>) getIntent().getSerializableExtra("carFoodList");
        //获取购物车中菜的总价格
        money=new BigDecimal(getIntent().getStringExtra("totalMoney"));
        //获取店铺的配送费
        distributionCost=new BigDecimal(getIntent().getStringExtra(
                "distributionCost"));
        initView();
        setData();
    }
    /**
     * 初始化界面控件
     */
    private void initView(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("订单");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tv_back = (TextView) findViewById(R.id.tv_back);
        lv_order= (ListView) findViewById(R.id.lv_order);
        tv_distribution_cost = (TextView) findViewById(R.id.tv_distribution_cost);
        tv_total_cost = (TextView) findViewById(R.id.tv_total_cost);
        tv_cost = (TextView) findViewById(R.id.tv_cost);
        tv_payment = (TextView) findViewById(R.id.tv_payment);
        // 返回键的点击事件
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //“去支付”按钮的点击事件
                final Dialog dialog = new Dialog(OrderActivity.this, R.style.Dialog_Style);
                dialog.setContentView(R.layout.qr_code);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (timer!=null){
                            timer.cancel();
                        }
                    }
                });
                dialog.show();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        MyOk.get("SuccessServlet", new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    String pay = jsonObject.getString("pay");
                                    if (pay.equals("success")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(OrderActivity.this, "扫描并支付成功", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        dialog.dismiss();
                                        timer.cancel();
                                        timer=null;
                                        startActivity(new Intent(OrderActivity.this,PaySuccessActivity.class));
                                        OrderActivity.this.finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                },2000,1000);
            }
        });
    }
    private Timer timer;
    /**
     * 设置界面数据
     */
    private void setData() {
        adapter=new OrderAdapter(this);
        lv_order.setAdapter(adapter);
        adapter.setData(carFoodList);
        tv_cost.setText("￥"+money);
        tv_distribution_cost.setText("￥"+distributionCost);
        tv_total_cost.setText("￥"+(money.add(distributionCost)));
    }
}
