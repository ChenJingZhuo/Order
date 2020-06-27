package com.cjz.order;

public class Fruit implements Apple,Orange{
    @Override
    public void showName(String name) {
        System.out.println(name);
    }
}
