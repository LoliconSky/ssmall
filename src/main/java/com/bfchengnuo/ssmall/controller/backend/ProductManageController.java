package com.bfchengnuo.ssmall.controller.backend;

import com.bfchengnuo.ssmall.common.Const;
import com.bfchengnuo.ssmall.common.ResponseCode;
import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.pojo.Product;
import com.bfchengnuo.ssmall.pojo.User;
import com.bfchengnuo.ssmall.service.IFileService;
import com.bfchengnuo.ssmall.service.IProductService;
import com.bfchengnuo.ssmall.service.IUserService;
import com.bfchengnuo.ssmall.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

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
    @Autowired
    private IFileService fileService;

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

    @PostMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,
                                 @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                 HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要管理员账号登陆");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            String path = request.getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map<String, String> data = Maps.newHashMap();
            data.put("uri", targetFileName);
            data.put("url", url);
            return ServerResponse.createBySuccess(data);
        } else {
            return ServerResponse.createByErrorMessage("无权操作");
        }
    }

    @PostMapping("richtext_img_upload.do")
    @ResponseBody
    public Map<String, Object> richtextImgUpload(HttpSession session,
                                                 @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        Map<String, Object> data = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            data.put("success", false);
            data.put("msg", "未登录，需要管理员账号登陆");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            // 富文本控件用的 simditor 对返回值有规范要求
            String path = request.getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                data.put("success", false);
                data.put("msg", "上传文件失败");
                return data;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            data.put("success", true);
            data.put("msg", "上传文件成功");
            data.put("file_path", url);
            // 和前端的约定
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        } else {
            data.put("success", false);
            data.put("msg", "无权操作");
        }
        return data;
    }
}
