package com.bfchengnuo.ssmall.service;

import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.pojo.Product;
import com.bfchengnuo.ssmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

/**
 * Created by 冰封承諾Andy on 2018/7/12.
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse getProductList(Integer pageNumber, Integer pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNumber, Integer pageSize);
}
