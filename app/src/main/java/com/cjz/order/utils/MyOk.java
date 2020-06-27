package com.cjz.order.utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MyOk {

    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();

    public static void get(String uri, Callback callback) {
        Request request = new Request.Builder()
                .url("http://192.168.100.22:8081/pay_war_exploded/"+uri)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
