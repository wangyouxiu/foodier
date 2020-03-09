package com.imooc.service;


import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * @version 1.0
 * @ClassName ItemService
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/19 15:40
 **/
public interface ItemService {

    /**
     * 根据商品id查询详情
     * @param ItemId
     * @return
     */
    Items queryItemById(String ItemId);

    /**
     * 根据商品id查询商品图片列表
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品id查询商品的评价等级数量
     * @param itemId
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品id查询用户评价(分页查询)
     * @param itemId
     * @param level
     * @return
     */
    PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 搜索商品
     * @param keyWords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItems(String keyWords, String sort, Integer page, Integer pageSize);

    /**
     * 根据三级分类搜索商品
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);

    /***
    * @Description 根据规格ids查询最新的购物车中商品数据（刷新渲染购物车中的商品数据）
    * @Param [specIds]
    * @Author wangyue
    * @Date 2020/2/29 15:28
    * @Return java.util.List<com.imooc.pojo.vo.ShopcartVO>
    **/
    List<ShopcartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据主键查询商品规格
     * @param specId
     * @return
     */
    ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品id获取主图
     * @param itemId
     * @return
     */
    String queryItemMainImgById(String itemId);

    /**
     * 扣除库存
     * @param specId
     * @param buyCounts
     */
    void decreaseItemSpecStock(String specId, int buyCounts);
}
