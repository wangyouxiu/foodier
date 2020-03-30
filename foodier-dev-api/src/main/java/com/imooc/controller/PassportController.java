package com.imooc.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.*;
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
 * @ClassName PassportController
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/16 15:43
 **/
@Api(value = "注册登录",tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value="用户名是否存在",notes = "用户名是否存在",httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value="用户注册",notes = "用户注册",httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();
        //0.判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPwd)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //1.查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        //2.密码长度不小于6位
        if (password.length() < 6) {
            return IMOOCJSONResult.errorMsg("密码长度不能小于6");
        }
        //3.判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            return IMOOCJSONResult.errorMsg("两次输入密码不一致");
        }
        //4.实现注册
        Users userResult = userService.createUser(userBO);
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult),true);

        //TODO 生成用户token，存入redis会话
        //同步购物车数据
        synchShopcatData(userResult.getId(), request, response);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value="用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        //0.判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //1.实现登录
        Users userResult = null;
        try {
            userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userResult == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult),true);

        //TODO 生成用户token，存入redis会话
        //同步购物车数据
        synchShopcatData(userResult.getId(), request, response);
        return IMOOCJSONResult.ok(userResult);
    }


    /**
     * 注册登录成功后，同步cookie和redis中的购物车数据
     */
    private void synchShopcatData(String userId,HttpServletRequest request, HttpServletResponse response){
        /**
         * 1.redis数据为空，
         *          cookie数据为空，不做操作
         *          cookie数据不为空，用cookie数据覆盖redis数据
         * 2.redis数据不为空，
         *          cookie数据为空，用redis覆盖cookie
         *          cookie数据不为空，用redis覆盖cookie
         */

        //获取redis数据
        String shopcatRedis = redisOperator.get(FOODIE_SHOPCAT + ":" + userId);
        //获取cookie数据
        String shopcatCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCAT, true);
        if (StringUtils.isBlank(shopcatRedis)) {
            if (StringUtils.isNotBlank(shopcatCookie)) {
                redisOperator.set(FOODIE_SHOPCAT + ":" + userId, shopcatCookie);
            }
        }else {
            if (StringUtils.isBlank(shopcatCookie)) {
                CookieUtils.setCookie(request, response, FOODIE_SHOPCAT, shopcatRedis, true);
            }else {
                //合并redis和cookie数据，同一商品以redis数据为准
                /**
                 * 1.已经存在的，把cookie中对应的数量，覆盖redis(参考京东)
                 * 2.该项商品标记为待删除，统一放入一个待删除的list
                 * 3.从cookie中清理所有的待删除list
                 * 4.合并redis和cookie中的数据
                 * 5.更新到redis和cookie中
                 */
                List<ShopcartBO> shopcartRedisList = JSON.parseArray(shopcatRedis, ShopcartBO.class);
                List<ShopcartBO> shopcartCookieList = JSON.parseArray(shopcatCookie, ShopcartBO.class);

                List<ShopcartBO> deleteList = new ArrayList<>();
                for (ShopcartBO shopcartRedis : shopcartRedisList) {
                    String redisSpecId = shopcartRedis.getSpecId();
                    for (ShopcartBO shopcartCookie : shopcartCookieList) {
                        String cookieSpecId = shopcartCookie.getSpecId();
                        if (cookieSpecId.equals(redisSpecId)) {
                            //同一商品数量以cookie为中，不累加，参考京东的做法
                            shopcartRedis.setBuyCounts(shopcartCookie.getBuyCounts());
                            //用于后面删除cookie中的数据
                            deleteList.add(shopcartCookie);
                        }
                    }
                }
                //删除cookie中的数据，剩下的数据是redis中没有的
                shopcartCookieList.removeAll(deleteList);
                //合并redis中不存在的数据
                shopcartRedisList.addAll(shopcartCookieList);
                //同步覆盖redis和cookie，做到同步
                CookieUtils.setCookie(request, response, FOODIE_SHOPCAT, JSON.toJSONString(shopcartRedisList), true);
                redisOperator.set(FOODIE_SHOPCAT + ":" + userId, JSON.toJSONString(shopcartRedisList));
            }
        }
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        userResult.setRealname(null);
        return userResult;
    }

    @ApiOperation(value = "退出登录",notes = "退出登录",httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, "user");
        //用户退出登录，需要清空购物车
        //分布式会话中需要清除用户数据
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCAT);
        return IMOOCJSONResult.ok();
    }
}
