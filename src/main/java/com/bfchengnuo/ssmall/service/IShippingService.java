package com.bfchengnuo.ssmall.service;

import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.pojo.Shipping;
import com.github.pagehelper.PageInfo;

/**
 * Created by 冰封承諾Andy on 2018/7/13.
 */
public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse<String> delete(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId, Integer pageNumber, Integer pageSize);
}
