package com.bfchengnuo.ssmall.controller.backend;

import com.bfchengnuo.ssmall.common.Const;
import com.bfchengnuo.ssmall.common.ResponseCode;
import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.pojo.Product;
import com.bfchengnuo.ssmall.pojo.User;
import com.bfchengnuo.ssmall.service.IProductService;
import com.bfchengnuo.ssmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 管理产品的接口
 * Created by 冰封承諾Andy on 2018/7/12.
 */
@Controller
@RequestMapping("manage/product")
public class ProductManageController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IProductService productService;

    @PostMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要管理员账号登陆");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("无权操作");
        }
    }


    @PostMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要管理员账号登陆");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            // 修改产品的销售状态
            return productService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("无权操作");
        }
    }

    @GetMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要管理员账号登陆");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("无权操作");
        }
    }

    @GetMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session,
                                  @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要管理员账号登陆");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.getProductList(pageNumber, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权操作");
        }
    }

    @GetMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,
                                        String productName,
                                        Integer productId,
                                        @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要管理员账号登陆");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.searchProduct(productName, productId, pageNumber, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权操作");
        }
    }
}
