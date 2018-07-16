package com.bfchengnuo.ssmall.dao;

import com.bfchengnuo.ssmall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单里的子项，包含有商品 id、数量、单价快照、总价格、图片快照等
 */
public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> getByOrderNoAndUserId(@Param("orderNo") Long orderNo,
                                          @Param("userId") Integer userId);

    void batchInsert(@Param("orderItems") List<OrderItem> orderItems);

    List<OrderItem> getByOrderNo(Long orderNo);
}