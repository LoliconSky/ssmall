package com.bfchengnuo.ssmall.service.impl;

import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.dao.ShippingMapper;
import com.bfchengnuo.ssmall.pojo.Shipping;
import com.bfchengnuo.ssmall.service.IShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 冰封承諾Andy on 2018/7/13.
 */
@Service("shippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int result = shippingMapper.insert(shipping);
        if (result > 0) {
            Map<String, Integer> map = Maps.newHashMap();
            map.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功", map);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> delete(Integer userId, Integer shippingId) {
        // 使用 userId 来避免横向越权
        int result = shippingMapper.deleteByShippingIdAndUserId(shippingId, userId);
        if (result > 0) {
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int result = shippingMapper.updateByShipping(shipping);
        if (result > 0) {
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        // 使用 userId 来避免横向越权
        Shipping shipping = shippingMapper.selectByShippingIdAndUserId(shippingId, userId);
        if (shipping != null) {
            return ServerResponse.createBySuccess("查询地址成功", shipping);
        }
        return ServerResponse.createByErrorMessage("查询地址失败");
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
