package com.imooc.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 我的订单列表
 * @version 1.0
 * @ClassName myOrdersVo
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/3/6 10:38
 **/
@Data
public class myOrdersVo {

    private String orderId;
    private Date createdTime;
    private Integer payMethod;
    private Integer realPayAmount;
    private Integer postAmount;
    private Integer orderStatus;
    private Integer isComment;

    private List<SubOrderItemsVo> subOrderItemList;

}
