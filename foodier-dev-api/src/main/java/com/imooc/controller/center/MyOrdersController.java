package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Orders;
import com.imooc.service.OrderService;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @ClassName MyOrdersController
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/3/6 11:09
 **/
@Api(value = "center - 用户订单",tags = {"用户中心，用户订单相接口"})
@RequestMapping("myorders")
@RestController
public class MyOrdersController extends BaseController {

    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "我的订单查询",notes = "我的订单查询",httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId", value = "userId", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "orderStatus", required = false)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "当前页码", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页展示的记录数", required = false)
            @RequestParam Integer pageSize){
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult result = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return IMOOCJSONResult.ok(result);
    }

    @ApiOperation(value = "商家发货",notes = "商家发货",httpMethod = "POST")
    @PostMapping("/deliver")
    public IMOOCJSONResult deliver(
            @ApiParam(name = "orderId", value = "orderId", required = true)
            @RequestParam String orderId){
        if (StringUtils.isBlank(orderId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户确认收货",notes = "用户确认收货",httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public IMOOCJSONResult confirmReceive(
            @ApiParam(name = "orderId", value = "orderId", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "userId", required = true)
            @RequestParam String userId){
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        IMOOCJSONResult result = checkUserOrder(orderId, userId);
        if (!result.isOK()) {
            return result;
        }
        boolean response = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!response) {
            return IMOOCJSONResult.errorMsg("删除订单失败");
        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户删除订单",notes = "用户删除订单",httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "orderId", value = "orderId", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "userId", required = true)
            @RequestParam String userId){
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        IMOOCJSONResult result = checkUserOrder(orderId, userId);
        if (!result.isOK()) {
            return result;
        }
        boolean response = myOrdersService.delete(userId, orderId);
        if (!response) {
            return IMOOCJSONResult.errorMsg("删除订单失败");
        }
        return IMOOCJSONResult.ok();
    }

    /**
     * 越权校验
     * @return
     */
    private IMOOCJSONResult checkUserOrder(String orderId,String userId){
        Orders orders = myOrdersService.queryMyOrder(userId, orderId);
        if (orders == null) {
            return IMOOCJSONResult.errorMsg("越权访问");
        }
        return IMOOCJSONResult.ok(orders);
    }

}
