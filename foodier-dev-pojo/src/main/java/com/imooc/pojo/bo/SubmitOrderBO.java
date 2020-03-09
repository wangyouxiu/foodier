package com.imooc.pojo.bo;

import lombok.Data;

/**
 * @version 1.0
 * @ClassName SubmitOrderBO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/29 21:14
 **/
@Data
public class SubmitOrderBO {

    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;
}
