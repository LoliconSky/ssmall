package com.bfchengnuo.ssmall.service.impl;

import com.bfchengnuo.ssmall.common.Const;
import com.bfchengnuo.ssmall.common.ResponseCode;
import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.dao.CategoryMapper;
import com.bfchengnuo.ssmall.dao.ProductMapper;
import com.bfchengnuo.ssmall.pojo.Category;
import com.bfchengnuo.ssmall.pojo.Product;
import com.bfchengnuo.ssmall.service.ICategoryService;
import com.bfchengnuo.ssmall.service.IProductService;
import com.bfchengnuo.ssmall.util.DateTimeUtil;
import com.bfchengnuo.ssmall.util.PropertiesUtil;
import com.bfchengnuo.ssmall.vo.ProductDetailVo;
import com.bfchengnuo.ssmall.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 冰封承諾Andy on 2018/7/12.
 */
@Service("productService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            // 将第一幅图片设置为主图
            String[] imgs = product.getSubImages().split(",");
            if (imgs.length > 0) {
                product.setMainImage(imgs[0]);
            }

            if (product.getId() != null) {
                // 更新操作
                int count = productMapper.updateByPrimaryKey(product);
                if (count > 0) {
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            } else {
                // 新增操作
                int count = productMapper.insert(product);
                if (count > 0) {
                    return ServerResponse.createBySuccessMessage("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        }
        return ServerResponse.createByErrorMessage("参数不正确");
    }

    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品已下架或者已删除");
        }
        // 构造 vo（这里仅仅是 Value object）
        ProductDetailVo vo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(vo);
    }

    @Override
    public ServerResponse getProductList(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVos = Lists.newArrayList();
        productList.forEach(p -> productListVos.add(assembleProductListVo(p)));
        // TODO 是不是有问题？
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNumber, Integer pageSize) {
        if (productName != null) {
            productName = "%" + productName + "%";
        }

        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVo> productListVos = Lists.newArrayList();
        productList.forEach(p -> productListVos.add(assembleProductListVo(p)));
        // TODO 是不是有问题？
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品已下架或者已删除");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            // 非在线状态
            return ServerResponse.createByErrorMessage("商品已下架或者已删除");
        }
        // 构造 vo（这里仅仅是 Value object）
        ProductDetailVo vo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(vo);
    }

    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNumber, Integer pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数错误");
        }
        List<Integer> categoryIds = Lists.newArrayList();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                // 没有该分类，并且还没有没有关键字，返回一个空集
                PageHelper.startPage(pageNumber, pageSize);
                ArrayList<ProductListVo> list = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(list);
                return ServerResponse.createBySuccess(pageInfo);
            }

            if (category != null) {
                categoryIds = categoryService.selectCategoryAndChildrenById(category.getId()).getData();
            }
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = "%" + keyword + "%";
        }
        PageHelper.startPage(pageNumber, pageSize);
        // 排序处理
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderArr = orderBy.split("_");
                PageHelper.orderBy(orderArr[0] + " " + orderArr[1]);
            }
        }
        List<Product> products = productMapper.selectByNameAndCategoryIds(
                StringUtils.isBlank(keyword) ? null : keyword,
                categoryIds.size() == 0 ? null : categoryIds);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        products.forEach(p -> productListVoList.add(assembleProductListVo(p)));
        // 分页
        PageInfo pageInfo = new PageInfo(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setStatus(product.getStatus());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://static.bfchengnuo.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category != null) {
            productDetailVo.setParentCategoryId(category.getParentId());
        } else {
            productDetailVo.setParentCategoryId(0);
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://static.bfchengnuo.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }
}
