package com.imooc.service;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.pojo.vo.myOrdersVo;

import java.util.List;

/**
 * @version 1.0
 * @ClassName OrderService
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/19 15:40
 **/
public interface OrderService {


    /**
     * 用于创建订单相关信息
     * @param submitOrderBO
     */
    OrderVO createOrder(SubmitOrderBO submitOrderBO, List<ShopcartBO> shopcartBOS);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    /***
    * @Description 定时任务，关闭订单
    * @Param []
    * @Author wangyue
    * @Date 2020/3/2 12:54
    * @Return void
    **/
    void closeOrder();
}
