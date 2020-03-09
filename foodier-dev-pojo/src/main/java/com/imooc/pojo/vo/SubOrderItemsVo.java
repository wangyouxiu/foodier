package com.imooc.pojo.vo;

import lombok.Data;

/**
 * @version 1.0
 * @ClassName SubOrderItemsVo
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/3/6 10:41
 **/
@Data
public class SubOrderItemsVo {

    private String itemId;
    private String itemName;
    private String itemImg;
    private String itemSpecId;
    private String itemSpecName;
    private Integer buyCounts;
    private Integer price;
}
