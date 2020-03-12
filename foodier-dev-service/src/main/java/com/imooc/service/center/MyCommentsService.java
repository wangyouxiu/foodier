package com.imooc.service.center;

import com.imooc.pojo.OrderItems;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.pojo.vo.MyCommentVO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @version 1.0
 * @ClassName UserService
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/16 15:34
 **/
public interface MyCommentsService {


    /**
     * 根据订单id查询关联的商品
     * @param orderId
     * @return
     */
    List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存用户评论
     */
    void saveComments(String userId, String orderId, List<OrderItemsCommentBO> list);

    /**
     * 查询用户历史评价
     * @param userId
     * @return
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);

}
