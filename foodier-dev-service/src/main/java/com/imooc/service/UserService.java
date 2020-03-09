package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;

/**
 * @version 1.0
 * @ClassName UserService
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/16 15:34
 **/
public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     * @param userBO
     * @return
     */
    Users createUser(UserBO userBO);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    Users queryUserForLogin(String username, String password);
}
