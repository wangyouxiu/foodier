package com.imooc.service.center;

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
}
