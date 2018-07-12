package com.bfchengnuo.ssmall.service.impl;

import com.bfchengnuo.ssmall.common.Const;
import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.common.TokenCache;
import com.bfchengnuo.ssmall.dao.UserMapper;
import com.bfchengnuo.ssmall.pojo.User;
import com.bfchengnuo.ssmall.service.IUserService;
import com.bfchengnuo.ssmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by 冰封承諾Andy on 2018/7/11.
 */
@Service("userServiceImpl")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int result = userMapper.checkUserName(username);
        if (result == 0) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String md5Pwd = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Pwd);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccess("注册成功");
    }

    /**
     * 校验用户的用户名或者邮箱是否不存在
     *
     * @param str  校验的值
     * @param type 校验类型：username email
     * @return 存在-校验失败；不存在-校验成功
     */
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            // 开始校验
            int result;
            if (Const.USERNAME.equals(type)) {
                result = userMapper.checkUserName(str);
                if (result > 0) {
                    return ServerResponse.createByErrorMessage("用户已存在");
                }
            } else if (Const.EMAIL.equals(type)) {
                result = userMapper.checkUserEmail(str);
                if (result > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数不正确");
        }
        return ServerResponse.createBySuccess("可以使用");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题为空");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int result = userMapper.checkAnswer(username, question, answer);
        if (result > 0) {
            // 验证通过，生成一个 UUID 作为令牌
            String token = UUID.randomUUID().toString();
            TokenCache.setKey("TOKEN_" + username, token);
            return ServerResponse.createBySuccess(token);
        }
        return ServerResponse.createByErrorMessage("答案错误");
    }

    @Override
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String token) {
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("需要token");
        }
        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String cacheToken = TokenCache.getVal("TOKEN_" + username);
        if (StringUtils.isBlank(cacheToken)) {
            return ServerResponse.createByErrorMessage("token 失效");
        }
        if (StringUtils.equals(token, cacheToken)) {
            // 验证通过，更新密码
            String md5Pwd = MD5Util.MD5EncodeUtf8(passwordNew);
            int result = userMapper.updatePasswordByUsername(username, md5Pwd);
            if (result > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else {
            return ServerResponse.createByErrorMessage("token 不匹配，请重新获取");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> restPassword(String passwordOld, String passwordNew, User user) {
        // 防止横向越权，要校验一下旧密码
        int count = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (count == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int result = userMapper.updateByPrimaryKeySelective(user);
        if (result > 0) {
            return  ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        // username 不能被更新；email 也要校验是否已存在（改动的情况下，也就是除自己外是否已存在）
        int result = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (result > 0) {
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }
        // 选择需要更新的内容
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int count = userMapper.updateByPrimaryKeySelective(updateUser);
        if (count > 0) {
            updateUser.setUsername(user.getUsername());
            return ServerResponse.createBySuccess("更新信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
