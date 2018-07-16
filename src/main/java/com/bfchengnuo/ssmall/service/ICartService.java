package com.bfchengnuo.ssmall.service;

import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.vo.CartVo;

/**
 * Created by 冰封承諾Andy on 2018/7/13.
 */
public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> selectOrUnselect(Integer userId, Integer productId, Integer checked);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}
