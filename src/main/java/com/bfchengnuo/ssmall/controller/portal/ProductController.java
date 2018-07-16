package com.bfchengnuo.ssmall.controller.portal;

import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.service.IProductService;
import com.bfchengnuo.ssmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 前台用的产品的接口
 * Created by 冰封承諾Andy on 2018/7/13.
 */
@Controller
@RequestMapping("product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> getDetail(Integer productId) {
        return productService.getProductDetail(productId);
    }

    /**
     * 查询和分页，包含对名字（模糊搜索）和类名的搜索
     */
    @GetMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(required = false, value = "keyword") String keyword,
                                         @RequestParam(required = false, value = "categoryId") Integer categoryId,
                                         @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return productService.getProductByKeywordCategory(keyword, categoryId, pageNumber, pageSize, orderBy);
    }
}
