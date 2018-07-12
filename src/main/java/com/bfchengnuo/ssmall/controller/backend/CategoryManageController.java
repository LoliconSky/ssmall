package com.bfchengnuo.ssmall.controller.backend;

import com.bfchengnuo.ssmall.common.Const;
import com.bfchengnuo.ssmall.common.ResponseCode;
import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.pojo.User;
import com.bfchengnuo.ssmall.service.ICategoryService;
import com.bfchengnuo.ssmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 商品分类管理模块
 * 这是给后台用的，需要登陆使用
 * Created by 冰封承諾Andy on 2018/7/12.
 */
@Controller
@RequestMapping("manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICategoryService categoryService;

    @PostMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,
                                      String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登陆");
        }
        ServerResponse response = userService.checkAdminRole(user);
        if (response.isSuccess()) {
            // 管理员，处理
            return categoryService.addCategory(categoryName, parentId);
        }
        return ServerResponse.createByErrorMessage("您没有权限操作");
    }

    @PostMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, String categoryName, Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登陆");
        }
        ServerResponse response = userService.checkAdminRole(user);
        if (response.isSuccess()) {
            return categoryService.updateCategoryName(categoryName, categoryId);
        }
        return ServerResponse.createByErrorMessage("您没有权限操作");
    }

    /**
     *  获取平级的类目列表(不递归)
     */
    @GetMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登陆");
        }
        ServerResponse response = userService.checkAdminRole(user);
        if (response.isSuccess()) {
            return categoryService.getChildrenParallelCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("您没有权限操作");
    }

    /**
     *  递归获取类目列表（本类目以及子类目的 id 集合）
     *  因为是给后台用的，不需要其他的多余信息
     */
    @GetMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登陆");
        }
        ServerResponse response = userService.checkAdminRole(user);
        if (response.isSuccess()) {
            // 递归查询，当前节点和其子节点
            return categoryService.selectCategoryAndChildrenById(categoryId);
        }
        return ServerResponse.createByErrorMessage("您没有权限操作");
    }
}
