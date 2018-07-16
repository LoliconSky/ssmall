package com.bfchengnuo.ssmall.dao;

import com.bfchengnuo.ssmall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 整个订单的po，包含订单的总金额、订单号、运费、支付时间、发货时间、是否完成等等
 */
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId,
                                   @Param("orderNo") Long orderNo);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectByUserId(Integer userId);
}