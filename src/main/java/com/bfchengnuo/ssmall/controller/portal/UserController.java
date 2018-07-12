package com.bfchengnuo.ssmall.controller.portal;

import com.bfchengnuo.ssmall.common.Const;
import com.bfchengnuo.ssmall.common.ResponseCode;
import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.pojo.User;
import com.bfchengnuo.ssmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 冰封承諾Andy on 2018/7/11.
 */
@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("login.do")
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @GetMapping("logout.do")
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @PostMapping("register.do")
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return userService.register(user);
    }

    @GetMapping("check_valid.do")
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return userService.checkValid(str, type);
    }

    @GetMapping("get_user_info.do")
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
    }

    @GetMapping("forget_get_question.do")
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return userService.selectQuestion(username);
    }

    @PostMapping("forget_check_answer.do")
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    @PostMapping("forget_reset_password.do")
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String token) {
        return userService.forgetRestPassword(username, passwordNew, token);
    }

    @PostMapping("reset_password.do")
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return userService.restPassword(passwordOld, passwordNew, user);
    }

    @PostMapping("update_information.do")
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = userService.updateInformation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @GetMapping("get_information.do")
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，需要进行登陆");
        }
        return userService.getInformation(currentUser.getId());
    }


}
