package com.imooc.pojo.bo;

import lombok.Data;

/**
 * @version 1.0
 * @ClassName AddressBO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/29 16:54
 **/
@Data
public class AddressBO {
    private String addressId;
    private String userId;
    private String receiver;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail;
}
