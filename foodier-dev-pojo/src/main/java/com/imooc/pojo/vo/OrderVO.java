package com.imooc.pojo.vo;

import com.imooc.pojo.bo.ShopcartBO;
import lombok.Data;

import java.util.List;

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
    private List<ShopcartBO> toBeRemovedShopcatList;
}
