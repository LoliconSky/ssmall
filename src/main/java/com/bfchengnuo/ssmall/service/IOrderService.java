package com.bfchengnuo.ssmall.service;

import com.bfchengnuo.ssmall.common.ServerResponse;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * Created by 冰封承諾Andy on 2018/7/14.
 */
public interface IOrderService {

    ServerResponse pay(Integer userId, Long orderNo, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    ServerResponse createOrder(Integer userId, Integer shippingId);

    ServerResponse cancel(Integer userId, Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse getOrderDetail(Integer userId, Long orderNo);

    ServerResponse getOrderList(Integer userId, Integer pageNumber, Integer pageSize);

    ServerResponse<PageInfo> manageList(Integer pageNumber, Integer pageSize);

    ServerResponse manageDetail(Long orderNo);

    ServerResponse<PageInfo> manageSearch(Long orderNo, Integer pageNumber, Integer pageSize);

    ServerResponse manageSendGoods(Long orderNo);
}
