package com.imooc.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mapper.OrdersMapperCustom;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.vo.myOrdersVo;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName MyOrdersServiceImpl
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/3/2 13:57
 **/
@Service
public class MyOrdersServiceImpl implements MyOrdersService {

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }
        PageHelper.startPage(page, pageSize);
        List<myOrdersVo> myOrdersVos =  ordersMapperCustom.queryMyOrders(map);
        return setterPagedGrid(myOrdersVos, page);
    }

    private PagedGridResult setterPagedGrid(List<?> list,Integer page) {
        PageInfo<?> pageInfo = new PageInfo(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setTotal(pageInfo.getPages());
        grid.setRecords(pageInfo.getTotal());
        grid.setRows(list);
        return grid;
    }
}
