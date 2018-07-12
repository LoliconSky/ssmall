package com.bfchengnuo.ssmall.service;

import com.bfchengnuo.ssmall.common.ServerResponse;
import com.bfchengnuo.ssmall.pojo.User;

/**
 * Created by 冰封承諾Andy on 2018/7/11.
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetRestPassword(String username, String passwordNew, String token);

    ServerResponse<String> restPassword(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer id);
}
