package com.imooc.pojo.vo;

import lombok.Data;

/**
 * @version 1.0
 * @ClassName SearchItemsVO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/27 10:01
 **/
@Data
public class SearchItemsVO {

    private String itemId;
    private String itemName;
    private Integer sellCounts;
    private String imgUrl;
    private Integer price;
}
