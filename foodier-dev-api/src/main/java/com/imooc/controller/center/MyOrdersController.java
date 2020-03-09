package com.imooc.controller.center;

import com.imooc.controller.BaseController;
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
}
