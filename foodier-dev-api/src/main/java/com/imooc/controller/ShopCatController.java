package com.imooc.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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
public class ShopCatController extends BaseController {

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestParam String userId,
                               @RequestBody ShopcartBO shopcartBo,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        // 前端用户登录的情况下，添加商品到购物车，会同时同步到购物车的redis缓存
        // 需要判断当前购物车中包含已经存在的商品，存在则累加
        String shopcartJson = redisOperator.get(FOODIE_SHOPCAT + ":" + userId);
        List<ShopcartBO> shopcartBOList = null;
        if (StringUtils.isNotBlank(shopcartJson)) {
            //redis中已经存在购物车
            shopcartBOList = JSON.parseArray(shopcartJson, ShopcartBO.class);
            //判断购物车中是否已经存在商品，已存在累加即可
            boolean isHaving = false;
            for (ShopcartBO sc : shopcartBOList) {
                String temSpecId = sc.getSpecId();
                if (temSpecId.equals(shopcartBo.getSpecId())) {
                    sc.setBuyCounts(sc.getBuyCounts() + shopcartBo.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopcartBOList.add(shopcartBo);
            }
        } else {
            //redis 中没有购物车
            shopcartBOList = new ArrayList<>();
            //添加商品到购物车中
            shopcartBOList.add(shopcartBo);
        }
        //覆盖现有redis中的购物车
        redisOperator.set(FOODIE_SHOPCAT + ":" + userId, JSON.toJSONString(shopcartBOList));
        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "删除购物车商品", notes = "删除购物车商品", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(@RequestParam String userId,
                               @RequestParam String itemSpecId,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        // 前端用户登录的情况下，删除购物车商品，会同时同步到购物车的redis缓存
        String shopcatStr = redisOperator.get(FOODIE_SHOPCAT + ":" + userId);
        List<ShopcartBO> shopcartList = null;
        if (StringUtils.isNotBlank(shopcatStr)) {
            shopcartList = JSON.parseArray(shopcatStr, ShopcartBO.class);
            for (ShopcartBO sc: shopcartList) {
                if (itemSpecId.equals(sc.getSpecId())) {
                    shopcartList.remove(sc);
                    break;
                }
            }
            redisOperator.set(FOODIE_SHOPCAT + ":" + userId,JSON.toJSONString(shopcartList));
        }
        return IMOOCJSONResult.ok();
    }

}
