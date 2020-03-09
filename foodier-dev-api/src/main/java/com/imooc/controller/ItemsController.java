package com.imooc.controller;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @version 1.0
 * @ClassName IndexController
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/16 15:43
 **/
@Api(value = "商品接口",tags = {"商品信息展示相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情",notes = "查询商品详情",httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult info(@ApiParam(name = "itemId", value = "商品id", required = true)
                                    @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgList= itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        ItemInfoVO itemInfoVo = new ItemInfoVO();
        itemInfoVo.setItem(item);
        itemInfoVo.setItemImgList(itemsImgList);
        itemInfoVo.setItemSpecList(itemsSpecList);
        itemInfoVo.setItemParams(itemsParam);
        return IMOOCJSONResult.ok(itemInfoVo);
    }

    @ApiOperation(value = "查询商品评价等级",notes = "查询商品评价等级",httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(@ApiParam(name = "itemId", value = "商品id", required = true)
                                @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        CommentLevelCountsVO commentLevelCountsVo= itemService.queryCommentCounts(itemId);
        return IMOOCJSONResult.ok(commentLevelCountsVo);
    }

    @ApiOperation(value = "查询商品评论",notes = "查询商品评论",httpMethod = "GET")
    @GetMapping("/comments")
    public IMOOCJSONResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评论级别", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "当前页码", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页展示的记录数", required = false)
            @RequestParam Integer pageSize){
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult comments = itemService.queryPagedComments(itemId, level, page, pageSize);
        return IMOOCJSONResult.ok(comments);
    }

    @ApiOperation(value = "商品搜索",notes = "商品搜索",httpMethod = "GET")
    @GetMapping("/search")
    public IMOOCJSONResult search(
            @ApiParam(name = "keywords", value = "搜索条件", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序方式", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "当前页码", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页展示的记录数", required = false)
            @RequestParam Integer pageSize){
        if (StringUtils.isBlank(keywords)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult items = itemService.searchItems(keywords, sort, page, pageSize);
        return IMOOCJSONResult.ok(items);
    }

    @ApiOperation(value = "根据三级分类搜索商品",notes = "根据三级分类搜索商品",httpMethod = "GET")
    @GetMapping("/catItems")
    public IMOOCJSONResult catItems(
            @ApiParam(name = "catId", value = "三级Id", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序方式", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "当前页码", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页展示的记录数", required = false)
            @RequestParam Integer pageSize){
        if (catId == null) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult items = itemService.searchItemsByThirdCat(catId, sort, page, pageSize);
        return IMOOCJSONResult.ok(items);
    }


    @ApiOperation(value = "刷新购物车商品",notes = "刷新购物车商品",httpMethod = "GET")
    @GetMapping("/refresh")
    public IMOOCJSONResult refresh(
            @ApiParam(name = "itemSpecIds", value = "规格id字符串", required = true)
            @RequestParam String itemSpecIds){
        if (StringUtils.isBlank(itemSpecIds)) {
            return IMOOCJSONResult.ok();
        }
        List<ShopcartVO> items = itemService.queryItemsBySpecIds(itemSpecIds);
        return IMOOCJSONResult.ok(items);
    }
}
