package com.bfchengnuo.ssmall.service;

import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.pojo.Category;

import java.util.List;

/**
 * Created by 冰封承諾Andy on 2018/7/12.
 */
public interface ICategoryService {
    ServerResponse addCategory(String name, Integer parentId);

    ServerResponse updateCategoryName(String categoryName, Integer categoryId);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);

    ServerResponse selectCategoryAndChildrenById(Integer categoryId);
}
