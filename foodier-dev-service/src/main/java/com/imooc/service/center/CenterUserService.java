package com.imooc.service.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;

/**
 * @version 1.0
 * @ClassName UserService
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/16 15:34
 **/
public interface CenterUserService {
    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);

    /**
     * 根据userId，更新用户信息
     * @param userId
     * @param centerUserBO
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 根据userId，更新用户头像信息
     * @param userId
     * @param imgUrl
     * @return
     */
    Users updateUserFace(String userId, String imgUrl);
}
