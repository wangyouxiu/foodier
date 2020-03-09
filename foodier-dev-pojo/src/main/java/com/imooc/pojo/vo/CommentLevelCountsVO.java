package com.imooc.pojo.vo;

import lombok.Data;

/**
 * 用于展示商品评价数的Vo
 * @version 1.0
 * @ClassName CommentLevelCountsVO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/25 13:46
 **/
@Data
public class CommentLevelCountsVO {
    private Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;
}
