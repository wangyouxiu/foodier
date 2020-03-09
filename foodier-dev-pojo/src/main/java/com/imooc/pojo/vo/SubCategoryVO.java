package com.imooc.pojo.vo;

import lombok.Data;

/**
 * @version 1.0
 * @ClassName SubCategoryVO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/20 11:17
 **/
@Data
public class SubCategoryVO {
    private Integer subId;
    private String subName;
    private Integer subType;
    private Integer subFatherId;
}
