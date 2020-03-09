package com.imooc.pojo.vo;

import lombok.Data;

/**
 * @version 1.0
 * @ClassName OrderVO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/3/1 16:19
 **/
@Data
public class OrderVO {
    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;
}
