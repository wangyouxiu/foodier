package com.imooc.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 二级分类Vo
 * @version 1.0
 * @ClassName CategoryVO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/20 11:12
 **/
@Data
public class CategoryVO {

    private Integer Id;
    private String name;
    private Integer type;
    private Integer fatherId;

    private List<SubCategoryVO> subCatList;
}
