package com.imooc.pojo.bo;

import lombok.Data;

/**
 * @version 1.0
 * @ClassName ShopcartBO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/27 23:21
 **/
@Data
public class ShopcartBO {

    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private Integer buyCounts;
    private String priceDiscount;
    private String priceNormal;

}
