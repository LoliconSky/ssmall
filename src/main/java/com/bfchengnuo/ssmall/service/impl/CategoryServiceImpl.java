package com.bfchengnuo.ssmall.service.impl;

import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.dao.CategoryMapper;
import com.bfchengnuo.ssmall.pojo.Category;
import com.bfchengnuo.ssmall.service.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 对品类的管理逻辑
 * Created by 冰封承諾Andy on 2018/7/12.
 */
@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String name, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(name)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        // 当前分类可用
        category.setStatus(true);

        int result = categoryMapper.insert(category);
        if (result > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类错误");
    }

    @Override
    public ServerResponse updateCategoryName(String categoryName, Integer categoryId) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int result = categoryMapper.updateByPrimaryKeySelective(category);
        if (result > 0) {
            return ServerResponse.createBySuccessMessage("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId) {
        List<Category> list = categoryMapper.selectCategoryChildrenByParentId(parentId);
        if (CollectionUtils.isEmpty(list)) {
            LOGGER.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 递归查询本节点的 id 和其子节点的 id
     * @param categoryId 要差些的节点 id
     * @return 本节点以及子节点 id 集合
     */
    @Override
    public ServerResponse selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        List<Integer> categoryIds = Lists.newArrayList();
        categorySet.forEach(category -> categoryIds.add(category.getId()));
        return ServerResponse.createBySuccess(categoryIds);
    }

    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        // 查找当前节点的子节点，即使没有 Mybatis 也不会返回一个 null 对象
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        categoryList.forEach(c -> findChildCategory(categorySet, c.getId()));
        return categorySet;
    }
}
