package com.imooc.pojo.vo;

import lombok.Data;

/**
 * @version 1.0
 * @ClassName ShopcartVO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/27 23:21
 **/
@Data
public class ShopcartVO {

    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private String priceDiscount;
    private String priceNormal;

}
