package com.bfchengnuo.ssmall.dao;

import com.bfchengnuo.ssmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId,
                                        @Param("productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdAndProductIds(@Param("userId") Integer userId,
                                    @Param("productList") List<String> productList);

    int checkedOrUncheckedProduct(@Param("userId") Integer userId,
                                  @Param("productId") Integer productId,
                                  @Param("checked") Integer checked);

    // null 无法赋值为 int 等基本数据类型
    int selectCartProductCount(Integer userId);
}