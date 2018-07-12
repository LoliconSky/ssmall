package com.bfchengnuo.ssmall.common;

/**
 * 通用的常量类
 * Created by 冰封承諾Andy on 2018/7/11.
 */
public class Const {
    public static final String CURRENT_USER = "current_user";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    // 避免使用太重的枚举类型
    public interface Role{
        // 普通用户
        int ROLE_CUSTOMER = 0;
        // 管理员
        int ROLE_ADMIN = 1;
    }
}
