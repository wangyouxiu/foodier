package com.imooc.service.center;

import com.imooc.pojo.Orders;
import com.imooc.utils.PagedGridResult;

/**
 * @version 1.0
 * @ClassName MyOrdersService
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/16 15:34
 **/
public interface MyOrdersService {

    /**
     * 查询我的订单列表
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    /**
     * 订单状态 --> 商家发货
     * @param orderId
     */
    void updateDeliverOrderStatus(String orderId);


    /**
     * 查询我的订单
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId, String orderId);

    /**
     * 更新订单状态 --> 确认收货
     * @param orderId
     * @return
     */
    boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单 --> 逻辑删除
     * @param userId
     * @param orderId
     * @return
     */
    boolean delete(String userId, String orderId);
}
