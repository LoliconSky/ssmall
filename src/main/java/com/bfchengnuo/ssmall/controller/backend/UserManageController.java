package com.bfchengnuo.ssmall.controller.backend;

import com.bfchengnuo.ssmall.common.Const;
import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.pojo.User;
import com.bfchengnuo.ssmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 冰封承諾Andy on 2018/7/12.
 */
@Controller
@RequestMapping("manage/user")
public class UserManageController {
    @Autowired
    private IUserService userService;

    @PostMapping("login.do")
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            }else {
                return ServerResponse.createByErrorMessage("无管理员权限");
            }
        }
        return response;
    }
}
