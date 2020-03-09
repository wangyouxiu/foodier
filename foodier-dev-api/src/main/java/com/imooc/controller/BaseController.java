package com.imooc.controller;

import org.springframework.stereotype.Controller;

import java.io.File;

/**
 * @version 1.0
 * @ClassName HelloController
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/15 16:41
 **/
@Controller
public class BaseController {
    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOODIER_SHOPCART = "shopcart";

    /**
     * 微信或支付宝支付成功后，订单中心的回调地址
     */
    String payReturnUrl = "http://iwtt7r.natappfree.cc/orders/notifyMerchantOrderPaid";
    /**
     * 订单中心创建订单接口
     */
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    /**
     * 用户上传头像存储地址
     * E:\work\images\foodier
     */
    public static final String IMAGE_USER_FACE_LOCATION = "E:"+File.separator+"work"+
                                                            File.separator+"images"+
                                                            File.separator+"foodier"+
                                                            File.separator+"faces";
}