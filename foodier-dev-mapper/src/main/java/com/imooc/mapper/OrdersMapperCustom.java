package com.imooc.mapper;


import com.imooc.pojo.vo.myOrdersVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {

    List<myOrdersVo> queryMyOrders(@Param("paramsMap") Map<String, Object> map);
}