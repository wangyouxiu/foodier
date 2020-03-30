package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.OrdersMapperCustom;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version 1.0
 * @ClassName OrderServiceImpl
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/3/1 11:29
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;

    @Autowired
    private Sid sid;

    @Autowired
    private RedisOperator redisOperator;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public OrderVO createOrder(SubmitOrderBO submitOrderBO, List<ShopcartBO> shopcartBOS) {

        String addressId = submitOrderBO.getAddressId();
        String userId = submitOrderBO.getUserId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        //包邮设置
        Integer postAmount = 0;
        String orderId = sid.nextShort();
        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);
        //1.新订单数据保存
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setReceiverAddress(userAddress.getProvince() + " " + userAddress.getCity() + " " + userAddress.getDistrict() + " " + userAddress.getDetail());
        orders.setPostAmount(postAmount);
        orders.setPayMethod(payMethod);
        orders.setLeftMsg(leftMsg);
        orders.setIsComment(YesOrNo.NO.type);
        orders.setIsDelete(YesOrNo.NO.type);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());
        //2.循环根据itemSpecIds保存订单商品信息表
        List<String> itemSpecIdList = new ArrayList(Arrays.asList(itemSpecIds.split(",")));
        //商品原价初始化
        Integer totalAmount = 0;
        //优惠后的实际支付价格累计
        Integer realPayAmount = 0;
        List<ShopcartBO> toBeRemovedShopcatList = new ArrayList<>();
        for (String specId : itemSpecIdList) {
            //整合redis之后，商品购买的数量重新从redis购物车中获取
            AtomicInteger buyCounts = new AtomicInteger(1);
            ShopcartBO shopcart = getBuyCountsFromShopcart(shopcartBOS, specId);
            Optional.ofNullable(shopcart).ifPresent(item -> buyCounts.set(item.getBuyCounts()));
            toBeRemovedShopcatList.add(shopcart);
            //2.1根据规格id获取规格，主要获取价格信息
            ItemsSpec itemSpec = itemService.queryItemSpecById(specId);
            totalAmount += itemSpec.getPriceNormal() * buyCounts.get();
            realPayAmount += itemSpec.getPriceDiscount() * buyCounts.get();
            //2.2根据商品id，获取商品信息以及图片信息
            String itemId = itemSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String mainImg = itemService.queryItemMainImgById(itemId);
            //2.3循环保存子订单到数据库
            OrderItems subOrderItems = new OrderItems();
            subOrderItems.setId(sid.nextShort());
            subOrderItems.setOrderId(orderId);
            subOrderItems.setItemId(itemId);
            subOrderItems.setItemImg(mainImg);
            subOrderItems.setItemName(item.getItemName());
            subOrderItems.setItemSpecId(specId);
            subOrderItems.setItemSpecName(itemSpec.getName());
            subOrderItems.setPrice(itemSpec.getPriceDiscount());
            subOrderItems.setBuyCounts(buyCounts.get());
            orderItemsMapper.insert(subOrderItems);
            //2.4在用户提交订单以后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(specId, buyCounts.get());
        }
        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(orders);
        //3.保存订单状态
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insertSelective(waitPayOrderStatus);
        //4.构建支付中心订单对象,controller中调用支付中心接口
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);
        //5.构建自定义订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        orderVO.setToBeRemovedShopcatList(toBeRemovedShopcatList);
        return orderVO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void closeOrder() {
        //查询超时未付款的订单，判断是否超时(1d)，超时则关闭订单
        OrderStatus queryOrder = new OrderStatus();
        queryOrder.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(queryOrder);
        for (OrderStatus os : list) {
            //获得订单创建时间
            Date createTime = os.getCreatedTime();
            //和当前时间进行对比
            int days = DateUtil.daysBetween(createTime, new Date());
            if (days >= 1) {
                //超过1天，关闭订单
                doCloseOrder(os.getOrderId());
            }

        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void doCloseOrder(String orderId) {
        OrderStatus os = new OrderStatus();
        os.setOrderId(orderId);
        os.setOrderStatus(OrderStatusEnum.CLOSE.type);
        os.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(os);
    }

    /**
     * 从redis购物车中获取商品，主要是要获取count
     * @param shopcartBOS
     * @param specId
     * @return
     */
    private ShopcartBO getBuyCountsFromShopcart(List<ShopcartBO> shopcartBOS,String specId) {
        for (ShopcartBO sc : shopcartBOS) {
            if (specId.equals(sc.getSpecId())) {
                return sc;
            }
        }
        return null;
    }
}
