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

    /**
     * 商品的在售状态
     */
    public enum ProductStatusEnum {
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

    /**
     * 订单的状态
     */
    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已付款"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSE(60, "订单关闭");

        private String value;
        private int code;
        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("么有找到对应的枚举");
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    /**
     * 支付方式（支付宝、微信）
     */
    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝");

        private String value;
        private int code;
        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    /**
     * 付款方式（货到付款、在线支付）
     */
    public enum PaymentTypeEnum {
        ONLINE_PAY(1, "在线支付");

        private String value;
        private int code;
        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static PaymentTypeEnum codeOf(Integer code) {
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    /**
     * 权限的分类（管理员和普通）
     * 避免使用太重的枚举类型
     */
    public interface Role {
        // 普通用户
        int ROLE_CUSTOMER = 0;
        // 管理员
        int ROLE_ADMIN = 1;
    }

    /**
     * 封装的商品的排序规则
     */
    public interface ProductListOrderBy {
        // 相比 list， set 的  contains 时间复杂度 o1 ，list on
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    /**
     * 购物车的状态
     * 全（不）选、是否大于库存
     */
    public interface Cart {
        int CHECKED = 1;
        int UN_CHECK = 2;

        // 数量限制是否成功，给前端用；不能大于库存
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    /**
     * 阿里支付接入的常用状态
     */
    public interface AlipayCallback {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }
}
