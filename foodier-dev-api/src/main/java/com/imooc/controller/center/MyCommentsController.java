package com.imooc.controller.center;

import com.alibaba.fastjson.JSON;
import com.imooc.controller.BaseController;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.pojo.vo.MyCommentVO;
import com.imooc.service.center.MyCommentsService;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.asm.IModelFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

/**
 * @version 1.0
 * @ClassName MyOrdersController
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/3/6 11:09
 **/
@Api(value = "center - 用户评价",tags = {"用户中心，用户评价相关接口"})
@Slf4j
@RequestMapping("mycomments")
@RestController
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;


    @ApiOperation(value = "用户评价",notes = "用户评价",httpMethod = "POST")
    @PostMapping("/pending")
    public IMOOCJSONResult pending(
            @ApiParam(name = "orderId", value = "orderId", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "userId", required = true)
            @RequestParam String userId){
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        IMOOCJSONResult checkResult = checkUserOrder(orderId, userId);
        if (!checkResult.isOK()) {
            return checkResult;
        }
        Orders myOrder = (Orders) checkResult.getData();
        if (myOrder.getIsComment().equals(YesOrNo.YES.type)) {
            return IMOOCJSONResult.errorMsg("该笔订单已评价");
        }
        List<OrderItems> orderItems =  myCommentsService.queryPendingComment(orderId);
        return IMOOCJSONResult.ok(orderItems);
    }



    @ApiOperation(value = "保存用户评价",notes = "保存用户评价",httpMethod = "POST")
    @PostMapping("/saveList")
    public IMOOCJSONResult saveList(
            @ApiParam(name = "orderId", value = "orderId", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "userId", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderItemList", value = "评价内容列表", required = true)
            @RequestBody List<OrderItemsCommentBO> orderItemList) {
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        IMOOCJSONResult checkResult = checkUserOrder(orderId, userId);
        if (!checkResult.isOK()) {
            return checkResult;
        }
        if (CollectionUtils.isEmpty(orderItemList)) {
            return IMOOCJSONResult.errorMsg("评价不能够为空");
        }
        myCommentsService.saveComments(userId, orderId, orderItemList);
        return IMOOCJSONResult.ok();
    }



    @ApiOperation(value = "查询用户历史评价",notes = "查询用户历史评价",httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId", value = "userId", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "当前页码", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页展示的记录数", required = false)
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult commentVOS = myCommentsService.queryMyComments(userId,page,pageSize);
        return IMOOCJSONResult.ok(commentVOS);
    }
}
