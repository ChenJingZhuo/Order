package com.cjz.order.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cjz.order.R;

public class PaySuccessActivity extends AppCompatActivity {

    private Button mBnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        mBnClose = findViewById(R.id.bn_close);
        mBnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaySuccessActivity.this.finish();
            }
        });
    }
}
