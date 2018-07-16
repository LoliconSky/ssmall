package com.bfchengnuo.ssmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

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

    public interface ProductListOrderBy{
        // 相比 list， set 的  contains 时间复杂度 o1 ，list on
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Cart{
        int CHECKED = 1;
        int UN_CHECK = 2;

        // 数量限制是否成功，给前端用；不能大于库存
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public enum ProductStatusEnum{
        ON_SALE(1, "在线");

        private int code;
        private String value;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
