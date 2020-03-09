package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;

import java.util.List;

/**
 * @version 1.0
 * @ClassName AddressService
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/19 15:40
 **/
public interface AddressService {

    /**
     * 根据userId查询用户全部地址
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 用户新增收件地址
     * @param addressBO
     */
    void addNewUserAddress(AddressBO addressBO);

    /**
     * 用户更新收件地址
     * @param addressBO
     */
    void updateUserAddress(AddressBO addressBO);
    /**
     * 用户删除收件地址
     * @param userId,addressId
     */
    void deleteUserAddress(String userId,String addressId);

    /**
     * 设置默认地址
     * @param userId
     * @param addressId
     */
    void updateUserAddressToBeDefault(String userId, String addressId);

    /**
     * 根据用户id和地址id查询具体的用户地址
     * @param userId
     * @param addressId
     * @return
     */
    UserAddress queryUserAddress(String userId, String addressId);
}
