package com.imooc.controller;

import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version 1.0
 * @ClassName ShopCatController
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/15 16:41
 **/
@Api(value = "购物车相关接口实现",tags = {"购物车相关接口实现"})
@RequestMapping("shopcart")
@RestController
public class ShopCatController {

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestParam String userId,
                               @RequestBody ShopcartBO shopcartBo,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        //TODO 前端用户登录的情况下，添加商品到购物车，会同时同步到购物车的redis缓存
        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "删除购物车商品", notes = "删除购物车商品", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(@RequestParam String userId,
                               @RequestParam String specId,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(specId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        //TODO 前端用户登录的情况下，删除购物车商品，会同时同步到购物车的redis缓存
        return IMOOCJSONResult.ok();
    }

}
