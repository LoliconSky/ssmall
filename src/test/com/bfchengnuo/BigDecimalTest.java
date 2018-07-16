package com.bfchengnuo;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 冰封承諾Andy on 2018/7/13.
 */
public class BigDecimalTest {

    @Test
    public void test1() {
        // false
        System.out.println(0.05 + 0.01);
    }

    @Test
    public void test2() {
        // false
        BigDecimal b1 = new BigDecimal(0.05);
        BigDecimal b2 = new BigDecimal(0.01);
        System.out.println(b1.add(b2));
    }

    @Test
    public void test3() {
        // true
        BigDecimal b1 = new BigDecimal("0.05");
        BigDecimal b2 = new BigDecimal("0.01");
        System.out.println(b1.add(b2));
        // Double.toString(0.05);
    }

    @Test
    public void test4() {
        System.out.println(0.05 + 0.01);
    }

}
