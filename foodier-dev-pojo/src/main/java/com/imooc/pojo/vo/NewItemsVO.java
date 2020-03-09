package com.imooc.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 * @ClassName NewItemsVO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/21 16:48
 **/
@Data
public class NewItemsVO {
    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;

    private List<SimpleItemVO> simpleItemList;
}
