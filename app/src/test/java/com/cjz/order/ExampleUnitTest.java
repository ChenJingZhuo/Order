package com.cjz.order;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testFruit(){
        Fruit fruit = new Fruit();
        fruit.showName("苹果");
        new Apple() {
            @Override
            public void showName(String name) {
                System.out.println(name);
            }
        }.showName("888");

        Orange orange = new Orange() {
            @Override
            public void showName(String name) {
                System.out.println(name);
            }
        };
        orange.showName("orange");

    }
}